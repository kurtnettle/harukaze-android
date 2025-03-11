package com.kurtnettle.harukaze.presentation.webview

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.SnackbarController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

fun saveFileToDownloads(
    context: Context,
    coroutineScope: CoroutineScope,
    fileName: String,
    vCardContent: String
) {
    coroutineScope.launch {
        val isSuccess = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10 or later (Scoped Storage - MediaStore API)
                saveUsingMediaStore(context, fileName, vCardContent) != null
            } else {
                // Android 9 or lower (Legacy Storage)
                saveUsingLegacyStorage(fileName, vCardContent) != null
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to save file '$fileName': ${e.message}")
            false
        }

        val messageRes = if (isSuccess) R.string.file_saved_success else R.string.file_saved_error
        val message = context.getString(messageRes, fileName)

        if (isSuccess) {
            SnackbarController.sendInfo(message)
        } else {
            SnackbarController.sendError(message)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
private suspend fun saveUsingMediaStore(
    context: Context,
    fileName: String,
    vCardContent: String
): Uri? = withContext(Dispatchers.IO) {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/vcard")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        try {
            resolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(vCardContent.toByteArray())
            }
            uri
        } catch (e: Exception) {
            Timber.e(e, "Failed to write file '$fileName' using MediaStore: ${e.message}")
            null
        }
    }
}

private suspend fun saveUsingLegacyStorage(
    fileName: String,
    vCardContent: String
): File? = withContext(Dispatchers.IO) {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    )

    if (!downloadsDir.exists()) downloadsDir.mkdirs()
    val file = File(downloadsDir, fileName)

    try {
        FileOutputStream(file).use { fos ->
            fos.write(vCardContent.toByteArray())
        }
        file
    } catch (e: Exception) {
        Timber.e(e, "Failed to write file '$fileName' using Legacy Storage: ${e.message}")
        null
    }
}