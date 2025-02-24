package com.kurtnettle.harukaze.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kurtnettle.harukaze.data.models.AppConfig
import com.kurtnettle.harukaze.data.models.GithubRelease
import com.kurtnettle.harukaze.data.models.ProjectData
import com.kurtnettle.harukaze.data.models.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber


class DataManager(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "app_data")

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val IS_DYNAMIC_MODE = booleanPreferencesKey("is_dynamic_mode")
        val APP_UPDATE_INFO = stringPreferencesKey("app_update_info")
        val PROJECTS = stringPreferencesKey("projects")
        val LAST_UPDATE_CHECK_TIMESTAMP = longPreferencesKey("last_update_ts")
        val APP_UPDATED_TIMESTAMP = longPreferencesKey("app_updated_ts")
        val APP_CONFIG_UPDATED_TIMESTAMP = longPreferencesKey("app_conf_updated_ts")
        val PROJECTS_UPDATED_TIMESTAMP = longPreferencesKey("projects_updated_ts")
        val IS_WEBVIEW_ENABLED = booleanPreferencesKey(name = "is_webview_enabled")
        val IS_TOPBAR_ANIM_ENABLED = booleanPreferencesKey(name = "is_topbar_anim_enabled")
        val APP_CONFIG = stringPreferencesKey("app_config")
    }

    suspend fun saveThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[Keys.THEME_MODE] = mode.name
        }
    }

    val themeModeFlow: Flow<ThemeMode> = context.dataStore.data
        .map { prefs ->
            prefs[Keys.THEME_MODE]?.let(ThemeMode::valueOf) ?: ThemeMode.AUTO
        }

    val dynamicColorFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[Keys.IS_DYNAMIC_MODE] ?: false
        }

    suspend fun getThemeMode(): ThemeMode {
        return context.dataStore.data
            .map { prefs ->
                prefs[Keys.THEME_MODE]?.let { ThemeMode.valueOf(it) } ?: ThemeMode.AUTO
            }
            .catch { e ->
                Timber.e(e, "Read failed: ${e.message}")
                emit(ThemeMode.AUTO)
            }
            .first()
    }

    suspend fun saveDynamicMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_DYNAMIC_MODE] = enabled
        }
    }

    suspend fun getDynamicMode(): Boolean {
        return try {
            context.dataStore.data.map { it[Keys.IS_DYNAMIC_MODE] ?: false }.first()
        } catch (e: Exception) {
            Timber.e(e, "Failed to retrieve WebView enabled status: ${e.message}")
            false
        }
    }

    suspend fun saveAppConfig(config: AppConfig) {
        val jsonString = Json.encodeToString(config)
        context.dataStore.edit { preferences ->
            preferences[Keys.APP_CONFIG] = jsonString
        }
    }

    suspend fun getAppConfig(): AppConfig? {
        return try {
            val jsonString = context.dataStore.data.map { it[Keys.APP_CONFIG] ?: "" }.first()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<AppConfig>(jsonString)
            } else {
                Timber.e("AppConfig is empty")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to read AppConfig: ${e.message}")
            null
        }
    }

    suspend fun saveWebViewEnabledStatus(enable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_WEBVIEW_ENABLED] = enable
        }
    }

    suspend fun getWebViewEnabledStatus(): Boolean {
        return try {
            context.dataStore.data.map { it[Keys.IS_WEBVIEW_ENABLED] ?: true }.first()
        } catch (e: Exception) {
            Timber.e(e, "Failed to retrieve WebView enabled status: ${e.message}")
            false
        }
    }

    suspend fun saveTopBarAnimEnabledStatus(enable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_TOPBAR_ANIM_ENABLED] = enable
        }
    }

    suspend fun getTopBarAnimEnabledStatus(): Boolean {
        return try {
            context.dataStore.data.map { it[Keys.IS_TOPBAR_ANIM_ENABLED] ?: true }.first()
        } catch (e: Exception) {
            Timber.e(e, "Failed to retrieve WebView enabled status: ${e.message}")
            false
        }
    }

    suspend fun saveAppUpdateInfo(data: GithubRelease) {
        val jsonString = Json.encodeToString(data)
        context.dataStore.edit { preferences ->
            preferences[Keys.APP_UPDATE_INFO] = jsonString
        }
    }

    suspend fun getAppUpdateInfo(): GithubRelease? {
        return try {
            val jsonString = context.dataStore.data.map { it[Keys.APP_UPDATE_INFO] ?: "" }.first()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString(jsonString)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveLastUpdateTimestamp(currentEpochTime: Long) {
        context.dataStore.edit { preferences ->
            preferences[Keys.LAST_UPDATE_CHECK_TIMESTAMP] = currentEpochTime
        }
    }

    suspend fun getLastUpdateTimestamp(): Long {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.LAST_UPDATE_CHECK_TIMESTAMP] ?: 0L
        }.catch { e ->
            Timber.e(e, "Read failed: ${e.message}")
            emit(0L)
        }.first()
    }


    suspend fun saveAppConfigUpdatedTimestamp(currentEpochTime: Long) {
        context.dataStore.edit { preferences ->
            preferences[Keys.APP_CONFIG_UPDATED_TIMESTAMP] = currentEpochTime
        }
    }

    suspend fun getAppConfigUpdatedTimestamp(): Long {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.APP_CONFIG_UPDATED_TIMESTAMP] ?: 0L
        }.catch { e ->
            Timber.e(e, "Read failed: ${e.message}")
            emit(0L)
        }.first()
    }

    suspend fun saveAppUpdatedTimestamp(currentEpochTime: Long) {
        context.dataStore.edit { preferences ->
            preferences[Keys.APP_UPDATED_TIMESTAMP] = currentEpochTime
        }
    }

    suspend fun getAppUpdatedTimestamp(): Long {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.APP_UPDATED_TIMESTAMP] ?: 0L
        }.catch { e ->
            Timber.e(e, "Read failed: ${e.message}")
            emit(0L)
        }.first()
    }

    suspend fun saveProjects(data: List<ProjectData>) {
        val jsonString = Json.encodeToString(data)
        context.dataStore.edit { preferences ->
            preferences[Keys.PROJECTS] = jsonString
        }
    }

    suspend fun getProjects(): List<ProjectData> {
        return try {
            val jsonString = context.dataStore.data.map { it[Keys.PROJECTS] ?: "" }.first()

            if (jsonString.isNotEmpty()) {
                Json.decodeFromString(jsonString)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveProjectUpdatedTimestamp(currentEpochTime: Long) {
        context.dataStore.edit { preferences ->
            preferences[Keys.PROJECTS_UPDATED_TIMESTAMP] = currentEpochTime
        }
    }

    suspend fun getProjectUpdatedTimestamp(): Long {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.PROJECTS_UPDATED_TIMESTAMP] ?: 0L
        }.catch { e ->
            Timber.e(e, "Read failed: ${e.message}")
            emit(0L)
        }.first()
    }
}
