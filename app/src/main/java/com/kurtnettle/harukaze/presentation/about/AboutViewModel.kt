package com.kurtnettle.harukaze.presentation.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurtnettle.harukaze.SnackbarController
import com.kurtnettle.harukaze.data.models.AppConfig
import com.kurtnettle.harukaze.data.models.ThemeMode
import com.kurtnettle.harukaze.data.repository.DataManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AboutViewModel(
    private val dataManager: DataManager
) : ViewModel() {
    private val _themeMode = MutableStateFlow(ThemeMode.AUTO)
    private val _isDynamicEnabled = MutableStateFlow(false)
    private val _appConfig = MutableStateFlow<AppConfig?>(null)
    private val _isWebViewEnabled = MutableStateFlow(true)
    private val _isTopBarAnimEnabled = MutableStateFlow(true)

    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()
    val isDynamicEnabled: StateFlow<Boolean> = _isDynamicEnabled.asStateFlow()
    val appConfig: StateFlow<AppConfig?> = _appConfig.asStateFlow()
    val isWebViewEnabled: StateFlow<Boolean> = _isWebViewEnabled.asStateFlow()
    val isTopBarAnimEnabled: StateFlow<Boolean> = _isTopBarAnimEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            coroutineScope {
                launch { getDynamicThemeMode() }
                launch { getThemeMode() }
                launch { getAppConfig() }
                launch { getWebViewEnabledStatus() }
                launch { getTopBarAnimEnabledStatus() }
            }
        }
    }

    suspend fun getThemeMode() {
        _themeMode.value = dataManager.getThemeMode()
    }

    private suspend fun getDynamicThemeMode() {
        _isDynamicEnabled.value = dataManager.getDynamicMode()
    }

    private suspend fun getAppConfig() {
        _appConfig.value = dataManager.getAppConfig()
    }

    private suspend fun getWebViewEnabledStatus() {
        _isWebViewEnabled.value = dataManager.getWebViewEnabledStatus()
    }

    private suspend fun getTopBarAnimEnabledStatus() {
        _isTopBarAnimEnabled.value = dataManager.getTopBarAnimEnabledStatus()
    }

    fun saveThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            try {
                _themeMode.update { mode }
                dataManager.saveThemeMode(mode)
            } catch (err: Exception) {
                SnackbarController.sendError("Failed to update settings.")
            }
        }
    }

    suspend fun toggleDynamicMode() {
        try {
            _isDynamicEnabled.update { !it }
            dataManager.saveDynamicMode(_isDynamicEnabled.value)
        } catch (err: Exception) {
            SnackbarController.sendError("Failed to update settings.")
        }

    }

    suspend fun toggleWebViewEnabled() {
        try {
            _isWebViewEnabled.update { !it }
            dataManager.saveWebViewEnabledStatus(_isWebViewEnabled.value)
            SnackbarController.sendInfo(
                "Project websites will be opened ${if (_isWebViewEnabled.value) "within the app" else "in external browser"}."
            )
        } catch (err: Exception) {
            SnackbarController.sendError("Failed to update settings.")
        }
    }

    suspend fun toggleTopBarAnimEnabled() {
        try {
            _isTopBarAnimEnabled.update { !it }
            dataManager.saveTopBarAnimEnabledStatus(_isTopBarAnimEnabled.value)
            SnackbarController.sendInfo(
                "${if (_isTopBarAnimEnabled.value) "Enabled" else "Disabled"} top app bar animations."
            )
        } catch (err: Exception) {
            SnackbarController.sendError("Failed to update settings.")
        }
    }
}
