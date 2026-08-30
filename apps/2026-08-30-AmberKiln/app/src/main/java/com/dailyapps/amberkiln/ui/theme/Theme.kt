package com.dailyapps.amberkiln.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Ember,
    onPrimary = Color.White,
    secondary = EmberDark,
    background = Ash,
    surface = Color.White,
    onBackground = SlateInk,
    onSurface = SlateInk
)

private val DarkColors = darkColorScheme(
    primary = Ember,
    onPrimary = Clay,
    secondary = EmberDark,
    background = Clay,
    surface = Color(0xFF4A2A12),
    onBackground = Ash,
    onSurface = Ash
)

@Composable
fun AmberKilnTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
