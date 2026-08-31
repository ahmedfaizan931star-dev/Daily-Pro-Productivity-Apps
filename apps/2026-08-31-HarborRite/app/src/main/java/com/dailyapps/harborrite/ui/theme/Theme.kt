package com.dailyapps.harborrite.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Tide,
    onPrimary = Color.White,
    secondary = TideDark,
    background = Foam,
    surface = Color.White,
    onBackground = Ink,
    onSurface = Ink
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7EC8C8),
    onPrimary = TideDark,
    secondary = Sand,
    background = TideDark,
    surface = Color(0xFF123844),
    onBackground = Foam,
    onSurface = Foam
)

@Composable
fun HarborRiteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
