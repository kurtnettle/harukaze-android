package com.kurtnettle.harukaze.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.kurtnettle.harukaze.R

val Roboto = FontFamily(
    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_black_italic, FontWeight.Black, FontStyle.Italic),

    Font(R.font.roboto_semibold, FontWeight.SemiBold),
    Font(R.font.roboto_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),

    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_bold_italic, FontWeight.Bold, FontStyle.Italic),

    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_medium_italic, FontWeight.Medium, FontStyle.Italic),

    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_regular_italic, FontWeight.Normal, FontStyle.Italic),

    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_light_italic, FontWeight.Light, FontStyle.Italic),

    Font(R.font.roboto_thin, FontWeight.Thin),
    Font(R.font.roboto_thin_italic, FontWeight.Thin, FontStyle.Italic)
)

val Typography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = Roboto),
    displayMedium = Typography().displayMedium.copy(fontFamily = Roboto),
    displaySmall = Typography().displaySmall.copy(fontFamily = Roboto),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = Roboto),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = Roboto),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = Roboto),
    titleLarge = Typography().titleLarge.copy(fontFamily = Roboto),
    titleMedium = Typography().titleMedium.copy(fontFamily = Roboto),
    titleSmall = Typography().titleSmall.copy(fontFamily = Roboto),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = Roboto),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = Roboto),
    bodySmall = Typography().bodySmall.copy(fontFamily = Roboto),
    labelLarge = Typography().labelLarge.copy(fontFamily = Roboto),
    labelMedium = Typography().labelMedium.copy(fontFamily = Roboto),
    labelSmall = Typography().labelSmall.copy(fontFamily = Roboto)
)

