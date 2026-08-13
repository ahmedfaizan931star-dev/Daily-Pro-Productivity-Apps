package com.dailyapps.luminafocus.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = LuminaPrimary,
    onPrimary = LuminaOnPrimary,
    secondary = LuminaSecondary,
    tertiary = LuminaTertiary,
    background = LuminaBackground,
    surface = LuminaSurface,
    onBackground = LuminaOnBackground,
    onSurface = LuminaOnSurface
)

private val DarkColorScheme = darkColorScheme(
    primary = LuminaPrimaryNight,
    onPrimary = LuminaOnPrimary,
    secondary = LuminaSecondary,
    tertiary = LuminaTertiary,
    background = LuminaBackgroundNight,
    surface = LuminaSurfaceNight,
    onBackground = LuminaOnBackgroundNight,
    onSurface = LuminaOnSurfaceNight
)

@Composable
fun LuminaFocusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
