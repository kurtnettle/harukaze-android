package com.kurtnettle.harukaze.presentation.home

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.webkit.URLUtil
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.SnackbarController
import com.kurtnettle.harukaze.data.models.ProjectData


@Composable
fun CardList(
    data: List<ProjectData>,
    onItemClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(items = data) { cardData ->
            CardItem(
                title = cardData.title,
                description = cardData.description,
                url = cardData.url,
                onItemClick = { onItemClick(cardData.url) }
            )
        }
    }
}

fun isUrlValid(url: String): Boolean {
    return URLUtil.isHttpsUrl(url) && Patterns.WEB_URL.matcher(url).matches()
}

suspend fun openUrl(context: Context, url: String) {
    if (!isUrlValid(url)) {
        SnackbarController.sendError("Invalid URL: ${url.take(50)}")
        return
    }

    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val clip = ClipData.newPlainText("URL", url)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(clip)
        SnackbarController.sendInfo("No browser found. URL copied to clipboard!")
    } catch (e: Exception) {
        SnackbarController.sendError("Error: ${e.localizedMessage ?: "Unknown error"}")
    }
}