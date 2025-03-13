package com.kurtnettle.harukaze.utils

import android.content.Context
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.SnackbarController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

fun copyToClipboard(
    context: Context,
    clipboard: ClipboardManager,
    coroutineScope: CoroutineScope,
    text: String
) {
    try {
        clipboard.setText(AnnotatedString(text))
        coroutineScope.launch {
            SnackbarController.sendInfo(context.getString(R.string.copied_to_clipboard))
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to copy text to clipboard: ${e.message}")
        coroutineScope.launch {
            SnackbarController.sendInfo(context.getString(R.string.failed_copied_to_clipboard))
        }
    }
}