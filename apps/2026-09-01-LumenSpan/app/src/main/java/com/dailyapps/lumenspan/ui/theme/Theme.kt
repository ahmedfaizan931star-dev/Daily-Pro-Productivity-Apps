package com.dailyapps.lumenspan.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Navy,
    onPrimary = Color.White,
    secondary = Teal,
    onSecondary = Color.White,
    tertiary = Amber,
    background = Sand,
    surface = Color.White,
    onBackground = Navy,
    onSurface = Navy
)

private val DarkColors = darkColorScheme(
    primary = Amber,
    onPrimary = Navy,
    secondary = Teal,
    background = Navy,
    surface = Deep,
    onBackground = Sand,
    onSurface = Sand
)

@Composable
fun LumenSpanTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
