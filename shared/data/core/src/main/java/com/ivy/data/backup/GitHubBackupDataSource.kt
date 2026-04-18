package com.ivy.data.backup

import android.util.Base64
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import timber.log.Timber
import javax.inject.Inject

class GitHubBackupDataSource @Inject constructor(
    private val httpClient: HttpClient,
) {
    companion object {
        private const val BACKUP_FILE_PATH = "ivy-backup/backup.json"
        private const val GITHUB_API_BASE = "https://api.github.com"
    }

    suspend fun uploadBackup(
        owner: String,
        repo: String,
        pat: String,
        jsonContent: String,
    ): Result<Unit> = runCatching {
        val existingSha = fetchFileSha(owner, repo, pat)
        val encodedContent = Base64.encodeToString(
            jsonContent.toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP
        )

        val requestBody = GitHubPutRequest(
            message = "Ivy Wallet auto-backup",
            content = encodedContent,
            sha = existingSha,
        )

        val response: HttpResponse = httpClient.put(
            "$GITHUB_API_BASE/repos/$owner/$repo/contents/$BACKUP_FILE_PATH"
        ) {
            header("Authorization", "Bearer $pat")
            header("X-GitHub-Api-Version", "2022-11-28")
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        check(
            response.status == HttpStatusCode.OK ||
                response.status == HttpStatusCode.Created
        ) {
            "GitHub API error: ${response.status}"
        }
    }.onFailure { Timber.e(it, "GitHub backup upload failed") }

    private suspend fun fetchFileSha(owner: String, repo: String, pat: String): String? {
        return runCatching {
            val response: HttpResponse = httpClient.get(
                "$GITHUB_API_BASE/repos/$owner/$repo/contents/$BACKUP_FILE_PATH"
            ) {
                header("Authorization", "Bearer $pat")
                header("X-GitHub-Api-Version", "2022-11-28")
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<GitHubGetResponse>().sha
            } else {
                null
            }
        }.getOrNull()
    }
}

@Serializable
private data class GitHubGetResponse(val sha: String)

@Serializable
private data class GitHubPutRequest(
    val message: String,
    val content: String,
    val sha: String? = null,
)
