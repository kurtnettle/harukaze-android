package com.kurtnettle.harukaze.presentation.about.app


import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.BuildConfig
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.presentation.about.settings.CustomHorizontalDivider
import com.kurtnettle.harukaze.presentation.about.settings.SectionRow
import com.kurtnettle.harukaze.presentation.main.MainViewModel
import com.kurtnettle.harukaze.utils.copyToClipboard
import com.kurtnettle.harukaze.utils.getWebViewVersion
import com.kurtnettle.harukaze.utils.isNewerAppVersion
import kotlinx.coroutines.launch


@Composable
fun AppSection(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current

    val appUpdateInfo by viewModel.appUpdateInfo.collectAsState()
    val lastUpdateTime by viewModel.lastUpdateTime.collectAsState()

    val lastUpdatedText = DateUtils.getRelativeTimeSpanString(lastUpdateTime).toString()

    val webViewVersion = getWebViewVersion(context = context)
    val appVersion = BuildConfig.VERSION_NAME
    val isNewAppAvail = appUpdateInfo?.let {
        isNewerAppVersion(current = appVersion, latest = it.tag_name)
    } == true

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SectionRow(
                icon = if (isNewAppAvail) Icons.Rounded.Bolt else Icons.Rounded.Info,
                label = stringResource(R.string.app_version),
                value = appVersion,
                showUpdateBadge = isNewAppAvail,
                appUpdateInfo = appUpdateInfo,
                onClick = {
                    coroutineScope.launch { viewModel.checkForAppUpdate(true) }
                }
            )

            CustomHorizontalDivider()

            SectionRow(
                icon = Icons.Rounded.Update,
                label = stringResource(R.string.project_last_updated),
                value = lastUpdatedText,
                onClick = {
                    coroutineScope.launch { viewModel.checkForProjectUpdate(true) }
                }
            )

            CustomHorizontalDivider()

            SectionRow(
                icon = Icons.Rounded.Public,
                label = stringResource(R.string.webview_version),
                value = webViewVersion,
                onClick = {
                    copyToClipboard(
                        context = context,
                        coroutineScope = coroutineScope,
                        clipboard = clipboard,
                        text = webViewVersion
                    )
                }
            )
        }
    }
}
