package com.dailyapps.sablequorum.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Pine,
    onPrimary = Color.White,
    primaryContainer = Fog,
    onPrimaryContainer = Ink,
    secondary = Brass,
    onSecondary = Ink,
    secondaryContainer = Color(0xFFE9DCC4),
    onSecondaryContainer = Ink,
    tertiary = Clay,
    onTertiary = Color.White,
    background = Parchment,
    onBackground = Ink,
    surface = Color(0xFFFAF7F1),
    onSurface = Ink,
    surfaceVariant = Fog,
    onSurfaceVariant = Pine,
    outline = Color(0xFF8A8378)
)

private val DarkColors = darkColorScheme(
    primary = BrassDark,
    onPrimary = Night,
    primaryContainer = Pine,
    onPrimaryContainer = Parchment,
    secondary = Moss,
    onSecondary = Night,
    tertiary = Clay,
    onTertiary = Color.White,
    background = Night,
    onBackground = Parchment,
    surface = NightCard,
    onSurface = Parchment,
    surfaceVariant = Color(0xFF24332D),
    onSurfaceVariant = Fog,
    outline = Color(0xFF8B918C)
)

@Composable
fun SableQuorumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
