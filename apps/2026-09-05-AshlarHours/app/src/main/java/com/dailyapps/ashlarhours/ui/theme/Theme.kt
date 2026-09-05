package com.dailyapps.ashlarhours.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = DeepWork,
    onPrimary = Color.White,
    secondary = Stone,
    onSecondary = Color.White,
    tertiary = Moss,
    background = Cream,
    onBackground = Ink,
    surface = Color(0xFFFAF6F0),
    onSurface = Ink,
    surfaceVariant = Sand,
    onSurfaceVariant = Color(0xFF4A4338)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9DC5AE),
    onPrimary = SlateDark,
    secondary = Sand,
    onSecondary = SlateDark,
    tertiary = Color(0xFFB7C9A8),
    background = SlateDark,
    onBackground = Color(0xFFE8E2D6),
    surface = CardDark,
    onSurface = Color(0xFFE8E2D6),
    surfaceVariant = Color(0xFF2A3028),
    onSurfaceVariant = Color(0xFFC9C2B4)
)

@Composable
fun AshlarHoursTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
