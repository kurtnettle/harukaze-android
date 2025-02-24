package com.kurtnettle.harukaze.presentation.about.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.data.models.GithubRelease
import com.kurtnettle.harukaze.data.models.ThemeMode
import com.kurtnettle.harukaze.presentation.about.app.VersionUpdateChip

sealed class TrailingContent {
    data class Switch(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit
    ) : TrailingContent()

    data class Dropdown(
        val defaultTheme: ThemeMode,
        val onThemeChange: (ThemeMode) -> Unit
    ) : TrailingContent()

    object None : TrailingContent()
}

@Composable
fun SectionRow(
    icon: ImageVector,
    label: String,
    value: String = "",
    modifier: Modifier = Modifier,
    showUpdateBadge: Boolean = false,
    appUpdateInfo: GithubRelease? = null,
    trailingContent: TrailingContent = TrailingContent.None,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon for $label",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            ValueContent(
                showUpdateBadge = showUpdateBadge,
                appUpdateInfo = appUpdateInfo,
                value = value
            )
        }

        when (trailingContent) {
            is TrailingContent.Switch -> {
                Switch(
                    checked = trailingContent.checked,
                    onCheckedChange = trailingContent.onCheckedChange
                )
            }

            is TrailingContent.Dropdown -> {
                ThemeModeDropDown(
                    currentTheme = trailingContent.defaultTheme,
                    onThemeChange = trailingContent.onThemeChange
                )
            }

            TrailingContent.None -> Unit
        }
    }
}

@Composable
private fun ValueContent(
    showUpdateBadge: Boolean,
    appUpdateInfo: GithubRelease?,
    value: String
) {
    if (showUpdateBadge) {
        appUpdateInfo?.let {
            VersionUpdateChip(
                currentVersion = value,
                appUpdateInfo = it
            )
        }
    } else {
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}