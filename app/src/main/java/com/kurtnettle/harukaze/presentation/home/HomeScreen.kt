package com.kurtnettle.harukaze.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kurtnettle.harukaze.presentation.main.MainViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(onCardClick: (String) -> Unit) {
    val viewModel: MainViewModel = koinViewModel()

    val cardDataList by viewModel.projectData.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Crossfade(
        targetState = Triple(isLoading, cardDataList.isEmpty(), cardDataList),
        animationSpec = tween(durationMillis = 400),
        label = "ProjectListTransition"
    ) { (loading, empty, data) ->
        when {
            loading -> LoadingText("Checking for new projects...")
            empty -> LoadingText("No projects available")
            else -> CardList(data, onItemClick = { url -> onCardClick(url) })
        }
    }
}

@Composable
fun LoadingText(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

