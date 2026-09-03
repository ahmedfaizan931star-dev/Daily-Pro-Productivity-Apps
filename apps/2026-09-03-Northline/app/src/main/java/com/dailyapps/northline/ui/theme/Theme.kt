package com.dailyapps.northline.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Pine,
    onPrimary = Color.White,
    secondary = Clay,
    onSecondary = Color.White,
    tertiary = Moss,
    background = Sand,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Fog,
    onSurfaceVariant = Pine
)

private val DarkColors = darkColorScheme(
    primary = Sage,
    onPrimary = Night,
    secondary = Clay,
    onSecondary = Night,
    tertiary = Moss,
    background = Night,
    onBackground = Sand,
    surface = NightCard,
    onSurface = Sand,
    surfaceVariant = Color(0xFF1D2C32),
    onSurfaceVariant = Fog
)

@Composable
fun NorthlineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = NorthlineTypography,
        content = content
    )
}
