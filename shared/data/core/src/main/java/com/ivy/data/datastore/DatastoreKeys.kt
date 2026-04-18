package com.ivy.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DatastoreKeys {
    @Deprecated("will be removed")
    val GITHUB_OWNER = stringPreferencesKey("github_backup_owner")

    @Deprecated("will be removed")
    val GITHUB_REPO = stringPreferencesKey("github_backup_repo")

    @Deprecated("will be removed")
    val GITHUB_PAT = stringPreferencesKey("github_backup_pat")

    @Deprecated("will be removed")
    val GITHUB_LAST_BACKUP_EPOCH_SEC =
        longPreferencesKey("github_backup_last_backup_time_epoch_sec")

    // GitHub auto-backup (v2) — new keys, the deprecated ones above were cleared on upgrade
    val GH_BACKUP_OWNER = stringPreferencesKey("gh_backup_owner")
    val GH_BACKUP_REPO = stringPreferencesKey("gh_backup_repo")
    val GH_BACKUP_PAT = stringPreferencesKey("gh_backup_pat")
    val GH_BACKUP_ENABLED = booleanPreferencesKey("gh_backup_enabled")
    val GH_BACKUP_LAST_TIMESTAMP = longPreferencesKey("gh_backup_last_timestamp")

    fun ivyFeature(key: String): Preferences.Key<Boolean> {
        return booleanPreferencesKey("feature_$key")
    }
}
