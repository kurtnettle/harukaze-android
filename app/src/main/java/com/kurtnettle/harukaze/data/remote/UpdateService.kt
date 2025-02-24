package com.kurtnettle.harukaze.data.remote

import com.kurtnettle.harukaze.data.models.ApiResult
import com.kurtnettle.harukaze.data.models.AppConfig
import com.kurtnettle.harukaze.data.models.GithubRelease
import com.kurtnettle.harukaze.data.models.ProjectResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import kotlinx.serialization.SerializationException
import okio.IOException
import timber.log.Timber


class UpdateService(
    private val httpClient: HttpClient,
    private val appConfig: AppConfig
) {

    suspend fun getAppUpdate(): ApiResult<GithubRelease> {
        return try {
            val response = httpClient.get(appConfig.app_update_url) {
                headers {
                    append(HttpHeaders.Accept, "application/vnd.github.v3+json")
                }
            }.body<GithubRelease>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch app update")
            handleError(e)
        }
    }

    suspend fun getProjectUpdate(url: String): ApiResult<ProjectResponse> {
        return try {
            val response = httpClient.get(url).body<ProjectResponse>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch project update")
            handleError(e)
        }
    }

    private fun <T> handleError(e: Exception): ApiResult<T> {
        return when (e) {
            is ClientRequestException -> {
                Timber.e("Client error: ${e.response.status.value}")
                ApiResult.Error(IOException("Client error: ${e.response.status.value}", e))
            }

            is ServerResponseException -> {
                Timber.e("Server error: ${e.response.status.value}")
                ApiResult.Error(IOException("Server error: ${e.response.status.value}", e))
            }

            is IOException -> {
                Timber.e(e, "Network error: ${e.message}")
                ApiResult.Error(IOException("Network error: ${e.message}", e))
            }

            is SerializationException -> {
                Timber.e(e, "Error parsing the response: ${e.message}")
                ApiResult.Error(IOException("Error parsing the response: ${e.message}", e))
            }

            else -> {
                Timber.e(e, "Unexpected error")
                ApiResult.Error(e)
            }
        }
    }
}
