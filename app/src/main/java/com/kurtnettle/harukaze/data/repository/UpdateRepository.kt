package com.kurtnettle.harukaze.data.repository

import com.kurtnettle.harukaze.BuildConfig
import com.kurtnettle.harukaze.data.models.ApiResult
import com.kurtnettle.harukaze.data.models.AppConfig
import com.kurtnettle.harukaze.data.models.GithubRelease
import com.kurtnettle.harukaze.data.models.ProjectResponse
import com.kurtnettle.harukaze.data.remote.UpdateService
import com.kurtnettle.harukaze.utils.Constants
import com.kurtnettle.harukaze.utils.NetworkUtils
import com.kurtnettle.harukaze.utils.isNewerAppVersion
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.IOException
import timber.log.Timber


class UpdateRepository(
    private val updateService: UpdateService,
    private val dataManager: DataManager,
    private val networkUtils: NetworkUtils,
    private val appConfig: AppConfig
) {

    private val mutex = Mutex()
    private val throttleInterval = Constants.UPDATE_THROTTLE_INTERVAL

    private suspend fun checkThrottle(): UpdateCheckResult.Throttled? {
        val lastUpdateRequestTime = dataManager.getLastUpdateTimestamp()
        return mutex.withLock {
            val currentTime = System.currentTimeMillis()
            val timeSinceLast = currentTime - lastUpdateRequestTime
            if (timeSinceLast < throttleInterval && lastUpdateRequestTime != 0L) {
                Timber.w("Update is throttled. Last update check: $lastUpdateRequestTime")
                UpdateCheckResult.Throttled(throttleInterval - timeSinceLast)
            } else {
                null
            }
        }
    }

    private suspend fun shouldSkipUpdate(isApp: Boolean = false): Boolean {
        val interval: Long
        val lastUpdateTime: Long

        if (isApp) {
            interval = Constants.APP_UPDATE_INTERVAL
            lastUpdateTime = dataManager.getAppUpdatedTimestamp()
        } else {
            interval = Constants.PROJECT_UPDATE_INTERVAL
            lastUpdateTime = dataManager.getProjectUpdatedTimestamp()
        }

        if (lastUpdateTime == 0L) return false

        val elapsed = System.currentTimeMillis() - lastUpdateTime
        return (elapsed < interval)
    }

    suspend fun fetchAppUpdate(
        force: Boolean = false,
        bypassThrottle: Boolean = false
    ): UpdateCheckResult {
        if (!networkUtils.isNetworkAvailable()) return UpdateCheckResult.Error.NoNetwork

        if (appConfig.app_update_url.isBlank()) return UpdateCheckResult.Error.NoUpdateUrl
        if (shouldSkipUpdate(true) && !force) {
            Timber.d("skipped app update")
            return UpdateCheckResult.UpdateSkipped
        }

        if (!bypassThrottle) {
            val throttleCheck = checkThrottle()
            if (throttleCheck != null) {
                Timber.d("App update check is throttled")
                return throttleCheck
            }
        }

        return when (val result = updateService.getAppUpdate()) {
            is ApiResult.Error -> handleServiceError(result.exception)

            is ApiResult.Success -> {
                Timber.d("got app update successfully")

                val currTs = System.currentTimeMillis()
                dataManager.saveLastUpdateTimestamp(currTs)
                dataManager.saveAppUpdatedTimestamp(currTs)
                handleAppUpdateResult(result.data)
            }
        }
    }

    suspend fun fetchProjectUpdate(
        force: Boolean = false,
        bypassThrottle: Boolean = false
    ): UpdateCheckResult {
        if (!networkUtils.isNetworkAvailable()) return UpdateCheckResult.Error.NoNetwork
        if (appConfig.project_update_url.isBlank()) return UpdateCheckResult.Error.NoUpdateUrl
        if (shouldSkipUpdate() && !force) {
            Timber.d("skipped project update")
            return UpdateCheckResult.UpdateSkipped
        }

        if (!bypassThrottle) {
            val throttleCheck = checkThrottle()
            if (throttleCheck != null) {
                Timber.d("Project update check is throttled")
                return throttleCheck
            }
        }

        return when (val result = updateService.getProjectUpdate(appConfig.project_update_url)) {
            is ApiResult.Error -> handleServiceError(result.exception)

            is ApiResult.Success -> {
                Timber.d("got projects update successfully")

                val currTs = System.currentTimeMillis()
                dataManager.saveLastUpdateTimestamp(currTs)
                dataManager.saveProjectUpdatedTimestamp(currTs)
                handleProjectUpdateResult(result.data)
            }
        }
    }

    private suspend fun handleAppUpdateResult(release: GithubRelease): UpdateCheckResult {
        val latestVersion = release.tag_name.removePrefix("v")
        val currentVersion = BuildConfig.VERSION_NAME

        return if (isNewerAppVersion(latestVersion, currentVersion)) {
            dataManager.saveAppUpdateInfo(release)
            UpdateCheckResult.UpdateAvailable(release)
        } else {
            UpdateCheckResult.NoUpdateAvailable
        }
    }

    private suspend fun handleProjectUpdateResult(response: ProjectResponse): UpdateCheckResult {
        val oldProject = dataManager.getProjects()

        return if (response.projects != oldProject) {
            dataManager.saveProjects(response.projects)
            UpdateCheckResult.UpdateAvailable(response)
        } else {
            UpdateCheckResult.NoUpdateAvailable
        }
    }

    private fun handleServiceError(exception: Throwable): UpdateCheckResult.Error {
        Timber.e("Error in update service $exception")

        return when (exception) {
            is ClientRequestException -> UpdateCheckResult.Error.ServerError(exception.response.status.value)
            is ServerResponseException -> UpdateCheckResult.Error.ServerError(exception.response.status.value)
            is IOException -> UpdateCheckResult.Error.NetworkError(exception)
            else -> UpdateCheckResult.Error.UnknownError(exception)
        }
    }
}


sealed class UpdateCheckResult {
    data class UpdateAvailable<out T>(val data: T) : UpdateCheckResult()
    object NoUpdateAvailable : UpdateCheckResult()
    object UpdateSkipped : UpdateCheckResult()
    data class Throttled(val retryAfterMillis: Long) : UpdateCheckResult()
    sealed class Error : UpdateCheckResult() {
        object NoNetwork : Error()
        object NoUpdateUrl : Error()
        data class ServerError(val code: Int) : Error()
        data class NetworkError(val cause: Throwable) : Error()
        data class UnknownError(val cause: Throwable) : Error()
    }
}
