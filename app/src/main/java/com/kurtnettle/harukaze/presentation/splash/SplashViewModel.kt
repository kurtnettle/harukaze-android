package com.kurtnettle.harukaze.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurtnettle.harukaze.data.config.RemoteConfigProvider
import com.kurtnettle.harukaze.data.repository.UpdateCheckResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val remoteConfig: RemoteConfigProvider
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _refreshedData = MutableStateFlow(false)
    private val _hideSplash = MutableStateFlow(false)
    private val _loadingText = MutableStateFlow("Fetching configuration...")
    private val _errorMessage = MutableStateFlow<String?>(null)

    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val refreshedData: StateFlow<Boolean> = _refreshedData.asStateFlow()
    val hideSplash: StateFlow<Boolean> = _hideSplash.asStateFlow()
    val loadingText: StateFlow<String> = _loadingText.asStateFlow()
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun refreshConfig(delay: Long = 400L) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            var err_msg: String? = null

            delay(delay) // Avoid flickering when Retry button is clicked

            when (val result = remoteConfig.getAppConfigUpdate()) {
                is UpdateCheckResult.Error.NoNetwork -> {
                    err_msg = "No Internet Connection"
                }

                is UpdateCheckResult.Error.UnknownError -> {
                    err_msg = result.cause.message.toString()
                }

                else -> {
                    _isLoading.value = false
                }
            }

            if (err_msg != null) _errorMessage.value = err_msg
            else _refreshedData.value = true

        }
    }
}
