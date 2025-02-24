package com.kurtnettle.harukaze.data.config

import com.kurtnettle.harukaze.data.models.AppConfig
import com.kurtnettle.harukaze.data.models.hasEmptyFields
import com.kurtnettle.harukaze.data.repository.DataManager
import com.kurtnettle.harukaze.data.repository.UpdateCheckResult
import com.kurtnettle.harukaze.utils.Constants
import com.kurtnettle.harukaze.utils.NetworkUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import timber.log.Timber

class RemoteConfigProvider(
    private val httpClient: HttpClient,
    private val networkUtils: NetworkUtils,
    private val dataManager: DataManager,
) {
    val config_url = Constants.APP_CONFIG_URL
    val interval = Constants.APP_CONFIG_UPDATE_INTERVAL

    suspend fun getAppConfigUpdate(): UpdateCheckResult {
        if (!networkUtils.isNetworkAvailable()) return UpdateCheckResult.Error.NoNetwork

        val last_check = dataManager.getAppConfigUpdatedTimestamp()
        if (last_check != 0L && (last_check < interval)) return UpdateCheckResult.UpdateSkipped

        val new_config: AppConfig?
        val old_config = dataManager.getAppConfig()

        try {
            new_config = httpClient.get(config_url).body<AppConfig>()
        } catch (e: Exception) {
            Timber.e(e, "failed to fetch app config")
            return UpdateCheckResult.Error.UnknownError(e)
        }

        if (new_config.hasEmptyFields()) {
            Timber.d("got empty url in app config")
            return UpdateCheckResult.Error.NoUpdateUrl
        } else {
            dataManager.saveAppConfigUpdatedTimestamp(System.currentTimeMillis())

            if (old_config != null && old_config == new_config) {
                Timber.d("no new app config update found")
                return UpdateCheckResult.NoUpdateAvailable
            } else {
                Timber.d("got app config update successfully")
                dataManager.saveAppConfig(new_config)
                return UpdateCheckResult.UpdateAvailable(new_config)
            }
        }
    }
}
