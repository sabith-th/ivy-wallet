package com.ivy.data.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GitHubAutoBackupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val gitHubBackupUseCase: GitHubBackupUseCase,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return gitHubBackupUseCase.performBackup().fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() },
        )
    }
}
