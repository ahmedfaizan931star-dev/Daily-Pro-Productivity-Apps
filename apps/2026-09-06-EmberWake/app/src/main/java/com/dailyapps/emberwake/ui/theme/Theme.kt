package com.dailyapps.emberwake.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Ember,
    onPrimary = Color.White,
    primaryContainer = EmberContainer,
    onPrimaryContainer = Ink,
    secondary = Pine,
    onSecondary = Color.White,
    secondaryContainer = PineContainer,
    onSecondaryContainer = Ink,
    background = Sand,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFEDE3DA),
    onSurfaceVariant = Slate,
    outline = Color(0xFFD0C4B8)
)

private val DarkColors = darkColorScheme(
    primary = EmberNight,
    onPrimary = Night,
    primaryContainer = EmberDark,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF8FCBB8),
    onSecondary = Night,
    secondaryContainer = Color(0xFF23463C),
    onSecondaryContainer = Color(0xFFD4E8E1),
    background = Night,
    onBackground = Color(0xFFF3E8DE),
    surface = NightCard,
    onSurface = Color(0xFFF3E8DE),
    surfaceVariant = Color(0xFF322B26),
    onSurfaceVariant = Color(0xFFC9BDB3),
    outline = Color(0xFF4A4039)
)

@Composable
fun EmberWakeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
