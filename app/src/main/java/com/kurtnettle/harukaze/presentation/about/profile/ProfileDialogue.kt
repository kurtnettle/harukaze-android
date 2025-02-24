package com.kurtnettle.harukaze.presentation.about.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kurtnettle.harukaze.R
import com.kurtnettle.harukaze.ui.theme.HarukazeTheme


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun ProfileDialogue(
    text: String,
    closeBtnTxt: String = "Close",
    onDismiss: () -> Unit,
    showDialog: Boolean = false,
    lottieIconName: String
) {
    val lottieComposition by rememberLottieComposition(
        spec = when (lottieIconName) {
            "sunglasses" -> LottieCompositionSpec.RawRes(R.raw.sunglass_lottie)
            "wave" -> LottieCompositionSpec.RawRes(R.raw.wave_lottie)
            "seal" -> LottieCompositionSpec.RawRes(R.raw.seal_lottie)
            else -> TODO()
        }
    )

    if (showDialog) Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .padding(16.dp)
                .widthIn(min = 280.dp, max = 400.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f))
                ) {
                    LottieAnimation(
                        composition = lottieComposition,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Button(
                    onClick = onDismiss,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(48.dp)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = closeBtnTxt,
                        style = MaterialTheme.typography.labelLarge,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileDialogue() {
    HarukazeTheme {
        ProfileDialogue(
            text = "Lorem Ipsum",
            onDismiss = {},
            closeBtnTxt = "Okay",
            showDialog = true,
            lottieIconName = "sunglasses",
        )
    }
}
