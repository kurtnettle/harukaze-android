package com.kurtnettle.harukaze.presentation.about.contact

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.presentation.about.settings.CustomHorizontalDivider
import com.kurtnettle.harukaze.ui.theme.HarukazeTheme

object ContactOption {
    const val github = "Github"
    const val gform = "GoogleForm"
    const val telegram = "Telegram"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSubmissionDialog(onDismiss: () -> Unit, onSelectOption: (String) -> Unit) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(16.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = "Report Issue",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    ReportOptionItem(title = "Github",
                        description = "Most Preferred method.",
                        imageResource = R.drawable.ic_github,
                        onClick = { onSelectOption(ContactOption.github) }
                    )

                    CustomHorizontalDivider()

                    ReportOptionItem(
                        title = "Google Form",
                        description = "Response time may be slower.",
                        iconResource = Icons.Default.Description,
                        onClick = { onSelectOption(ContactOption.gform) }
                    )

                    CustomHorizontalDivider()

                    ReportOptionItem(
                        title = "Telegram",
                        description = "Generally receives a quicker response",
                        imageResource = R.drawable.ic_telegram_no_bg,
                        onClick = { onSelectOption(ContactOption.telegram) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = onDismiss
                ) {
                    Text("Close", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun ReportOptionItem(
    title: String,
    description: String,
    imageResource: Int? = null,
    iconResource: ImageVector? = null,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
            ) {
                if (imageResource != null) {
                    Image(
                        painter = painterResource(id = imageResource),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                } else if (iconResource != null) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        imageVector = iconResource,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReportSubmissionDialog() {
    HarukazeTheme {
        ReportSubmissionDialog(onDismiss = {}, onSelectOption = {})
    }
}
