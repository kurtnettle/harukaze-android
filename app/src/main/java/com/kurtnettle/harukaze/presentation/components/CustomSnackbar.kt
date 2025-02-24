package com.kurtnettle.harukaze.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.SnackbarType
import com.kurtnettle.harukaze.ui.theme.NebulaGreen


@Composable
fun CustomSnackbar(
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(hostState = snackbarHostState, snackbar = { data ->
        val snackbarEvent = data.visuals.message.split("|||")
        val message = snackbarEvent[0]
        val type = if (snackbarEvent.size > 1) {
            SnackbarType.valueOf(snackbarEvent[1])
        } else {
            SnackbarType.INFO
        }

        val backgroundColor = when (type) {
            SnackbarType.INFO -> MaterialTheme.colorScheme.primaryContainer
            SnackbarType.SUCCESS -> NebulaGreen
            SnackbarType.WARN -> MaterialTheme.colorScheme.tertiaryContainer
            SnackbarType.ERROR -> MaterialTheme.colorScheme.errorContainer
        }

        val contentColor = when (type) {
            SnackbarType.INFO -> MaterialTheme.colorScheme.onPrimaryContainer
            SnackbarType.SUCCESS -> Color.White.copy(alpha = 0.9f)
            SnackbarType.WARN -> MaterialTheme.colorScheme.onTertiaryContainer
            SnackbarType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        }

        Snackbar(
            modifier = Modifier.padding(8.dp),
            contentColor = contentColor,
            containerColor = backgroundColor,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = message)
        }
    })
}
