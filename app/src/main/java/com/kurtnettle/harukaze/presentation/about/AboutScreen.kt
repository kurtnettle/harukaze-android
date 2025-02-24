package com.kurtnettle.harukaze.presentation.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.data.models.DeveloperProfile
import com.kurtnettle.harukaze.presentation.about.app.AppSection
import com.kurtnettle.harukaze.presentation.about.contact.LinkRow
import com.kurtnettle.harukaze.presentation.about.profile.ProfileSection
import com.kurtnettle.harukaze.presentation.about.settings.SettingsSection
import com.kurtnettle.harukaze.presentation.main.MainViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun AboutScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val aboutViewModel: AboutViewModel = koinViewModel()

    val profile = DeveloperProfile()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileSection(profile = profile)

        AppSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            viewModel = viewModel
        )

        Spacer(Modifier.height(24.dp))

        SettingsSection(
            modifier = Modifier.padding(horizontal = 16.dp),
            aboutViewModel = aboutViewModel
        )

        LinkRow(viewModel = aboutViewModel)
    }
}
