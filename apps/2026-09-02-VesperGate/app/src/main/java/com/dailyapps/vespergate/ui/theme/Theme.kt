package com.dailyapps.vespergate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Amber,
    onPrimary = Dusk,
    secondary = Sage,
    background = Dusk,
    surface = DuskContainer,
    onBackground = Mist,
    onSurface = Mist
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF5B3A8C),
    onPrimary = Color.White,
    secondary = Color(0xFF2F6B57),
    background = Color(0xFFF6F1EA),
    surface = Color.White,
    onBackground = Color(0xFF1B1430),
    onSurface = Color(0xFF1B1430)
)

@Composable
fun VesperGateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
