package com.kurtnettle.harukaze.presentation.about.contact

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource


@Composable
fun ChipItem(
    label: String,
    iconResId: Int? = null,
    icon: ImageVector? = null,
    contentDescription: String,
    onClick: () -> Unit
) {
    AssistChip(
        label = { Text(label) },
        onClick = onClick,
        leadingIcon = {
            iconResId?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(AssistChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } ?: icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(AssistChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
