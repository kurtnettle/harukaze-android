package com.kurtnettle.harukaze.presentation.about.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.data.models.GithubRelease
import com.kurtnettle.harukaze.presentation.home.openUrl
import kotlinx.coroutines.launch

@Composable
fun VersionUpdateChip(
    currentVersion: String,
    appUpdateInfo: GithubRelease
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(2.dp)
            .clickable {
                coroutineScope.launch {
                    openUrl(context, appUpdateInfo.html_url)
                }
            }
    ) {
        Text(
            text = currentVersion,
            style = MaterialTheme.typography.bodyMedium,
        )
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = "Update available",
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = appUpdateInfo.tag_name, style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "(tap to update)",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}