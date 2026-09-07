package com.dailyapps.keelstone.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = CopperSoft,
    onPrimary = Slate900,
    secondary = Seafoam,
    onSecondary = Slate900,
    background = Slate900,
    onBackground = Mist,
    surface = Slate800,
    onSurface = Mist,
    surfaceVariant = Color(0xFF1E3A44),
    onSurfaceVariant = Fog
)

private val LightColors = lightColorScheme(
    primary = Copper,
    onPrimary = Color.White,
    secondary = Seafoam,
    onSecondary = Color.White,
    background = Mist,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFD7E4DE),
    onSurfaceVariant = Color(0xFF3E5450)
)

@Composable
fun KeelStoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
