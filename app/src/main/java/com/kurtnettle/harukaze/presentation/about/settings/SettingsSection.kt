package com.kurtnettle.harukaze.presentation.about.settings

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.BeachAccess
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.presentation.about.AboutViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    aboutViewModel: AboutViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    val defaultTheme by aboutViewModel.themeMode.collectAsState()
    val isDynamicEnabled by aboutViewModel.isDynamicEnabled.collectAsState()
    val isWebViewEnabled by aboutViewModel.isWebViewEnabled.collectAsState()
    val isTopBarAnimEnabled by aboutViewModel.isTopBarAnimEnabled.collectAsState()

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
                icon = Icons.Rounded.Animation,
                label = stringResource(R.string.enable_topbar_anim_label),
                value = stringResource(R.string.enable_topbar_anim_desc),
                onClick = {
                    coroutineScope.launch { aboutViewModel.toggleTopBarAnimEnabled() }
                },
                trailingContent = TrailingContent.Switch(
                    checked = isTopBarAnimEnabled,
                    onCheckedChange = {
                        coroutineScope.launch { aboutViewModel.toggleTopBarAnimEnabled() }
                    }
                ),
            )

            CustomHorizontalDivider()

            SectionRow(
                icon = Icons.Rounded.Public,
                label = stringResource(R.string.enable_webview_label),
                value = stringResource(R.string.enable_webview_desc),
                onClick = {
                    coroutineScope.launch { aboutViewModel.toggleWebViewEnabled() }
                },
                trailingContent = TrailingContent.Switch(
                    checked = isWebViewEnabled,
                    onCheckedChange = {
                        coroutineScope.launch { aboutViewModel.toggleWebViewEnabled() }
                    }
                )
            )

            CustomHorizontalDivider()

            if (Build.VERSION.SDK_INT >= 31) {
                SectionRow(
                    icon = Icons.Rounded.ColorLens,
                    label = stringResource(R.string.enable_dynamic_color_label),
                    value = stringResource(R.string.enable_dynamic_color_desc),
                    onClick = {
                        coroutineScope.launch { aboutViewModel.toggleDynamicMode() }
                    },
                    trailingContent = TrailingContent.Switch(
                        checked = isDynamicEnabled,
                        onCheckedChange = {
                            coroutineScope.launch { aboutViewModel.toggleDynamicMode() }
                        }
                    )
                )

                CustomHorizontalDivider()
            }

            SectionRow(
                icon = Icons.Rounded.BeachAccess,
                label = stringResource(R.string.theme_mode_label),
                value = stringResource(R.string.theme_mode_desc),
                trailingContent = TrailingContent.Dropdown(
                    defaultTheme = defaultTheme,
                    onThemeChange = { selectedMode ->
                        aboutViewModel.saveThemeMode(selectedMode)
                    }
                ),
            )
        }
    }
}

@Composable
fun CustomHorizontalDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.outline,
        thickness = 0.5.dp
    )
}
