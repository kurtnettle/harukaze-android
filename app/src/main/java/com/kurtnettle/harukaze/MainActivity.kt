package com.kurtnettle.harukaze

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kurtnettle.harukaze.data.models.AppConfig
import com.kurtnettle.harukaze.data.models.ThemeMode
import com.kurtnettle.harukaze.data.repository.DataManager
import com.kurtnettle.harukaze.di.updateModule
import com.kurtnettle.harukaze.presentation.main.MainScreen
import com.kurtnettle.harukaze.presentation.splash.SplashScreen
import com.kurtnettle.harukaze.presentation.splash.SplashViewModel
import com.kurtnettle.harukaze.ui.theme.HarukazeTheme
import com.kurtnettle.harukaze.utils.DebugTreeWithTag
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.loadKoinModules
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val database: DataManager by inject()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(DebugTreeWithTag())

        enableEdgeToEdge()

        setContent {
            val themeMode by database.themeModeFlow.collectAsStateWithLifecycle(initialValue = ThemeMode.AUTO)
            val isDynamicEnabled by database.dynamicColorFlow.collectAsStateWithLifecycle(
                initialValue = false
            )
            val isSystemDark =
                (themeMode == ThemeMode.AUTO && isSystemInDarkTheme()) || themeMode == ThemeMode.NIGHT

            var appConfig by remember { mutableStateOf<AppConfig?>(null) }
            val splashViewModel: SplashViewModel = koinViewModel()
            val refreshedData by splashViewModel.refreshedData.collectAsState()
            val hideSplash by splashViewModel.hideSplash.collectAsState()

            HarukazeTheme(
                darkTheme = isSystemDark,
                dynamicColor = isDynamicEnabled
            ) {

                LaunchedEffect(Unit) {
                    appConfig = database.getAppConfig()
                    if (appConfig == null) {
                        splashViewModel.refreshConfig()
                        Timber.d("Config not found locally, Refreshing configs")
                    } else {
                        appConfig?.let { loadKoinModules(updateModule(it)) }
                    }
                }

                LaunchedEffect(refreshedData) {
                    Timber.d("refreshedData: $refreshedData")
                    appConfig = database.getAppConfig()
                    appConfig?.let { loadKoinModules(updateModule(it)) }
                }

                if (!hideSplash) SplashScreen(true)
                if (appConfig != null) MainScreen(true)
            }
        }
    }
}