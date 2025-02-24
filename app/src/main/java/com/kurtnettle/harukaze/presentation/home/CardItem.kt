package com.kurtnettle.harukaze.presentation.home

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.SnackbarController
import kotlinx.coroutines.launch


@Composable
fun rememberShareHandler(): (String) -> Unit {
    val context: Context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val shareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        coroutineScope.launch {
            SnackbarController.sendInfo("Thanks for sharing! ✨")
        }
    }

    return { url ->
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share via")
            if (chooserIntent.resolveActivity(context.packageManager) != null) {
                shareLauncher.launch(chooserIntent)
            } else {
                coroutineScope.launch {
                    SnackbarController.sendInfo("No sharing options available")
                }
            }
        } catch (e: Exception) {
            coroutineScope.launch {
                SnackbarController.sendError("Error: ${e.localizedMessage ?: "Failed to share"}")
            }
        }
    }
}

@Composable
fun CardItem(
    title: String,
    description: String,
    url: String,
    onItemClick: (String) -> Unit
) {
    val shareUrl = rememberShareHandler()
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .shadow(4.dp, shape = MaterialTheme.shapes.large)
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth()
            .heightIn(min = 160.dp, max = 220.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                role = Role.Button,
                onClick = { onItemClick(url) }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            IconButton(
                onClick = {
                    if (!isUrlValid(url)) coroutineScope.launch {
                        SnackbarController.sendError(
                            "Invalid URL: ${url.take(50) + if (url.length > 50) "..." else ""}"
                        )
                    } else shareUrl(url)
                }, modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { heading() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = description.ifEmpty { "" },
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        fontFamily = FontFamily(
                            Font(R.font.lora_regular),
                            Font(R.font.lora_medium, FontWeight.Medium)
                        )
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}