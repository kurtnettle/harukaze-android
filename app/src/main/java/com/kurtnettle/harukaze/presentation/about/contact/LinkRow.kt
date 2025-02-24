package com.kurtnettle.harukaze.presentation.about.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.presentation.about.AboutViewModel
import com.kurtnettle.harukaze.presentation.home.openUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LinkRow(viewModel: AboutViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val appConfig by viewModel.appConfig.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    fun openUrlIfNotNull(url: String?) {
        url?.let {
            coroutineScope.launch {
                openUrl(context, it)
            }
        }
    }

    if (showDialog) {
        ReportSubmissionDialog(onDismiss = { showDialog = false },
            onSelectOption = { selectedOption ->
                when (selectedOption) {
                    ContactOption.telegram -> openUrlIfNotNull(appConfig?.telegram_group_url)
                    ContactOption.github -> openUrlIfNotNull("${appConfig?.repo_url}/issues")
                    ContactOption.gform -> openUrlIfNotNull(appConfig?.google_form_url)
                }
                showDialog = false
            })
    }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        ChipItem(
            label = "Source Code",
            iconResId = R.drawable.ic_github,
            contentDescription = "Tap to Check Source Code",
            onClick = { openUrlIfNotNull(appConfig?.repo_url) }
        )

        ChipItem(
            label = "Report Issues",
            icon = Icons.Rounded.BugReport,
            contentDescription = "Tap to Report an Issue",
            onClick = { showDialog = true }
        )

        ChipItem(
            label = "Join Telegram Group",
            iconResId = R.drawable.ic_telegram_no_bg,
            contentDescription = "Tap to Join Telegram Group",
            onClick = { openUrlIfNotNull(appConfig?.telegram_group_url) }
        )
    }
}