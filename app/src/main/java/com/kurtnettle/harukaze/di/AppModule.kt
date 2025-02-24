package com.kurtnettle.harukaze.di

import com.kurtnettle.harukaze.data.config.RemoteConfigProvider
import com.kurtnettle.harukaze.data.models.AppConfig
import com.kurtnettle.harukaze.data.remote.KtorWebClientProvider
import com.kurtnettle.harukaze.data.remote.UpdateService
import com.kurtnettle.harukaze.data.repository.DataManager
import com.kurtnettle.harukaze.data.repository.UpdateRepository
import com.kurtnettle.harukaze.presentation.about.AboutViewModel
import com.kurtnettle.harukaze.presentation.main.MainViewModel
import com.kurtnettle.harukaze.presentation.splash.SplashViewModel
import com.kurtnettle.harukaze.utils.NetworkUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {
    single { DataManager(androidContext()) }
    single { KtorWebClientProvider.httpClient }
    single { NetworkUtils(androidContext()) }
    single { RemoteConfigProvider(get(), get(), get()) }
    viewModel {
        SplashViewModel(remoteConfig = get())
    }

    viewModel {
        AboutViewModel(dataManager = get())
    }
}

fun updateModule(appConfig: AppConfig) = module {
    single { appConfig }
    single { UpdateService(get(), get()) }
    single { UpdateRepository(get(), get(), get(), get()) }

    viewModel {
        MainViewModel(
            dataManager = get(),
            updateRepository = get(),
            remoteConfigProvider = get()
        )
    }
}
