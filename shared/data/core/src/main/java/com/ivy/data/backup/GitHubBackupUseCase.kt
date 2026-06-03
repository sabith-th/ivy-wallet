package com.ivy.data.backup

import androidx.datastore.preferences.core.edit
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ivy.data.datastore.DatastoreKeys
import com.ivy.data.datastore.IvyDataStore
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GitHubBackupUseCase @Inject constructor(
    private val dataStore: IvyDataStore,
    private val backupDataUseCase: BackupDataUseCase,
    private val gitHubBackupDataSource: GitHubBackupDataSource,
    private val workManager: WorkManager,
) {
    companion object {
        const val WORK_NAME = "IVY_GITHUB_AUTO_BACKUP_V2"
    }

    suspend fun getConfig(): GitHubBackupConfig {
        val prefs = dataStore.data.first()
        return GitHubBackupConfig(
            owner = prefs[DatastoreKeys.GH_BACKUP_OWNER] ?: "",
            repo = prefs[DatastoreKeys.GH_BACKUP_REPO] ?: "",
            pat = prefs[DatastoreKeys.GH_BACKUP_PAT] ?: "",
            enabled = prefs[DatastoreKeys.GH_BACKUP_ENABLED] ?: false,
            lastBackupTimestamp = prefs[DatastoreKeys.GH_BACKUP_LAST_TIMESTAMP] ?: 0L,
        )
    }

    suspend fun saveConfig(config: GitHubBackupConfig) {
        dataStore.edit { prefs ->
            prefs[DatastoreKeys.GH_BACKUP_OWNER] = config.owner
            prefs[DatastoreKeys.GH_BACKUP_REPO] = config.repo
            prefs[DatastoreKeys.GH_BACKUP_PAT] = config.pat
            prefs[DatastoreKeys.GH_BACKUP_ENABLED] = config.enabled
        }
        if (config.enabled && config.isValid()) {
            scheduleAutoBackup()
        } else {
            cancelAutoBackup()
        }
    }

    suspend fun performBackup(): Result<Unit> {
        val config = getConfig()
        if (!config.isValid()) return Result.failure(IllegalStateException("GitHub backup not configured"))

        val json = backupDataUseCase.generateJsonBackup()
        val result = gitHubBackupDataSource.uploadBackup(
            owner = config.owner,
            repo = config.repo,
            pat = config.pat,
            jsonContent = json,
        )

        if (result.isSuccess) {
            dataStore.edit { prefs ->
                prefs[DatastoreKeys.GH_BACKUP_LAST_TIMESTAMP] = System.currentTimeMillis()
            }
        }

        return result
    }

    suspend fun ensureScheduled() {
        val config = getConfig()
        if (config.enabled && config.isValid()) {
            scheduleAutoBackup()
        }
    }

    private fun scheduleAutoBackup() {
        @Suppress("MagicNumber")
        val backoffMinutes = 30L
        val request = PeriodicWorkRequestBuilder<GitHubAutoBackupWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, backoffMinutes, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    private fun cancelAutoBackup() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}

data class GitHubBackupConfig(
    val owner: String,
    val repo: String,
    val pat: String,
    val enabled: Boolean,
    val lastBackupTimestamp: Long,
)

fun GitHubBackupConfig.isValid(): Boolean = owner.isNotBlank() && repo.isNotBlank() && pat.isNotBlank()
