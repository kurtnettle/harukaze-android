package com.kurtnettle.harukaze.presentation.main

import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurtnettle.harukaze.SnackbarController
import com.kurtnettle.harukaze.data.config.RemoteConfigProvider
import com.kurtnettle.harukaze.data.models.GithubRelease
import com.kurtnettle.harukaze.data.models.ProjectData
import com.kurtnettle.harukaze.data.models.ProjectResponse
import com.kurtnettle.harukaze.data.repository.DataManager
import com.kurtnettle.harukaze.data.repository.UpdateCheckResult
import com.kurtnettle.harukaze.data.repository.UpdateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val dataManager: DataManager,
    private val updateRepository: UpdateRepository,
    private val remoteConfigProvider: RemoteConfigProvider
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    private val _projectData = MutableStateFlow<List<ProjectData>>(emptyList())
    private val _appUpdateInfo = MutableStateFlow<GithubRelease?>(null)
    private val _lastUpdateTime = MutableStateFlow(0L)

    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val lastUpdateTime: StateFlow<Long> = _lastUpdateTime.asStateFlow()
    val projectData: StateFlow<List<ProjectData>> = _projectData.asStateFlow()
    val appUpdateInfo: StateFlow<GithubRelease?> = _appUpdateInfo.asStateFlow()

    init {
        viewModelScope.launch {
            loadData()
            checkForProjectUpdate(bypassThrottle = true)
            checkForAppUpdate(bypassThrottle = true)
            checkForAppConfigUpdate()
        }
    }

    private fun getRemainingDuration(duration: Long): String {
        return DateUtils.formatElapsedTime(duration / 1000)
    }

    private suspend fun updateLastProjectUpdateTime() {
        _lastUpdateTime.value = dataManager.getProjectUpdatedTimestamp()
    }

    private suspend fun checkForAppConfigUpdate() {
        remoteConfigProvider.getAppConfigUpdate()
    }


    fun checkForAppUpdate(
        force: Boolean = false,
        bypassThrottle: Boolean = false
    ) {
        viewModelScope.launch {
            val result =
                updateRepository.fetchAppUpdate(force = force, bypassThrottle = bypassThrottle)
            handleUpdateResult(result, "app")
        }
    }

    suspend fun checkForProjectUpdate(
        force: Boolean = false,
        bypassThrottle: Boolean = false
    ) {
        val result =
            updateRepository.fetchProjectUpdate(force = force, bypassThrottle = bypassThrottle)
        updateLastProjectUpdateTime()
        handleUpdateResult(result, "project")
    }

    private fun handleUpdateAvailable(data: Any?, updateType: String) {
        when (updateType) {
            "project" -> _projectData.value = (data as ProjectResponse).projects
            "app" -> _appUpdateInfo.value = data as GithubRelease
        }
    }

    private suspend fun handleUpdateResult(result: UpdateCheckResult, updateType: String) {
        when (result) {
            UpdateCheckResult.NoUpdateAvailable -> SnackbarController.sendInfo("No $updateType update found")
            UpdateCheckResult.Error.NoNetwork -> SnackbarController.sendError("No network connection")
            UpdateCheckResult.Error.NoUpdateUrl -> SnackbarController.sendError("No $updateType update URL is set")
            is UpdateCheckResult.UpdateSkipped -> Unit
            is UpdateCheckResult.Error.NetworkError -> SnackbarController.sendError(result.cause.message.toString())
            is UpdateCheckResult.Error.ServerError -> SnackbarController.sendError("Something went wrong with the remote source")
            is UpdateCheckResult.Error.UnknownError -> SnackbarController.sendError("An unknown error occurred. Pray for the Dev.")
            is UpdateCheckResult.Throttled -> SnackbarController.sendWarn(
                "Please wait ${getRemainingDuration(result.retryAfterMillis)} before checking again"
            )

            is UpdateCheckResult.UpdateAvailable<*> -> {
                handleUpdateAvailable(result.data, updateType)
                SnackbarController.sendSuccess("New $updateType update found!")
            }
        }
    }

    private suspend fun loadData() {
        _isLoading.value = true
        try {
            val (projects, appUpdateInfo) = withContext(Dispatchers.IO) {
                val projects = dataManager.getProjects()
                val updateInfo = dataManager.getAppUpdateInfo()
                projects to updateInfo
            }

            _projectData.value = projects
            _appUpdateInfo.value = appUpdateInfo
        } catch (e: Exception) {
            SnackbarController.sendError("An error occurred: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }
}
