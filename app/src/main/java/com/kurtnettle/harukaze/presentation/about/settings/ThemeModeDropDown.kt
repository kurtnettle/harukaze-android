package com.kurtnettle.harukaze.presentation.about.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.data.models.ThemeMode

@Composable
fun ThemeModeDropDown(
    currentTheme: ThemeMode,
    onThemeChange: ((ThemeMode) -> Unit)?
) {
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (isDropDownExpanded) 180f else 0f)

    val themeOptions = listOf(
        ThemeMode.AUTO to Icons.Filled.BrightnessAuto,
        ThemeMode.NIGHT to Icons.Filled.DarkMode,
        ThemeMode.LIGHT to Icons.Filled.LightMode,
    )

    fun formatThemeName(theme: ThemeMode) =
        theme.name.lowercase().replaceFirstChar { it.titlecase() }

    Column {
        Row(
            modifier = Modifier
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    shape = MaterialTheme.shapes.medium
                )
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .clickable(
                    role = Role.Button,
                    onClick = { isDropDownExpanded = !isDropDownExpanded }
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = formatThemeName(currentTheme),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = if (isDropDownExpanded) "Collapse menu" else "Expand menu",
                modifier = Modifier
                    .rotate(rotation)
                    .size(18.dp),
            )
        }

        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = { isDropDownExpanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            themeOptions.forEachIndexed { index, (type, icon) ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "${type.name} mode icon",
                                tint = if (type == currentTheme) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = formatThemeName(type),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (type == currentTheme) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                if (type == currentTheme) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "${type.name} mode selected",
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    },
                    onClick = {
                        isDropDownExpanded = false
                        if (onThemeChange != null) onThemeChange(type)
                    },
                    modifier = Modifier
                        .background(
                            if (type == currentTheme) MaterialTheme.colorScheme.surfaceContainerHighest
                            else Color.Transparent
                        )
                )
                if (index < themeOptions.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}
