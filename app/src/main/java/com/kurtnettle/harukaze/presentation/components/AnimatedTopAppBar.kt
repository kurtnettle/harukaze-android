package com.kurtnettle.harukaze.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kurtnettle.harukaze.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopAppBar() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.leaves_lottie))
    var currentIndex by remember { mutableIntStateOf(0) }
    val textList: List<@Composable () -> Unit> = listOf(
        {
            Text(
                text = stringResource(R.string.app_name),
            )
        }, {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name_meaning),
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(
                        Font(R.font.lora_medium, FontWeight.Medium)
                    ),
                )
                LottieAnimation(
                    composition, modifier = Modifier.size(36.dp)
                )
            }
        }
    )

    LaunchedEffect(Unit) {
        // 6s --- 6s --- 18s (repeat)
        while (true) {
            currentIndex = 0
            delay(6 * 1000)
            currentIndex = 1
            delay(6 * 1000)
            currentIndex = 0
            delay(18 * 1000)
        }
    }

    TopAppBar(
        modifier = Modifier.shadow(elevation = 8.dp),
        title = {
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    val exitDuration = 600
                    val enterDuration = 600

                    val enter = slideInHorizontally(
                        animationSpec = tween(
                            durationMillis = enterDuration,
                            delayMillis = exitDuration,
                            easing = FastOutSlowInEasing
                        ),
                        initialOffsetX = { -it }) +
                            fadeIn(tween(enterDuration))

                    val exit = slideOutHorizontally(
                        animationSpec = tween(
                            durationMillis = exitDuration,
                            easing = LinearOutSlowInEasing
                        ),
                        targetOffsetX = { it }) +
                            fadeOut(tween(200))

                    enter togetherWith exit
                }) { index ->
                Box(Modifier.wrapContentSize()) {
                    textList[index]()
                }
            }
        }
    )
}