package com.kurtnettle.harukaze.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// TODO: Add Custom Light Color Scheme (one day for sure)
val LightColorScheme = lightColorScheme()

val DarkColorScheme = darkColorScheme(
    primary = LunarSilver,
    onPrimary = CosmicBlack,
    primaryContainer = CraterGrey,
    onPrimaryContainer = LunarSilver,

    secondaryContainer = CraterGrey,
    onSecondaryContainer = LunarSilver,

    background = CosmicBlack,
    onBackground = LunarSilver,

    surface = AsteroidGrey,
    onSurface = LunarSilver,
    surfaceContainer = AsteroidGrey,
    surfaceVariant = CraterGrey,
    onSurfaceVariant = LunarSilver.copy(alpha = 0.8f),

    error = EclipseRed,
    onError = LunarSilver,
    errorContainer = MagmaCore,
    onErrorContainer = LunarSilver,

    outline = MeteorGray,
    outlineVariant = CraterGrey,

    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun HarukazeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}