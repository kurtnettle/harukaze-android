package com.kurtnettle.harukaze.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kurtnettle.harukaze.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NormalTopAppBar() {
    TopAppBar(
        title = {
            Box(Modifier.wrapContentSize()) {
                Text(stringResource(R.string.app_name))
            }
        },
        modifier = Modifier
            .shadow(8.dp)
    )
}