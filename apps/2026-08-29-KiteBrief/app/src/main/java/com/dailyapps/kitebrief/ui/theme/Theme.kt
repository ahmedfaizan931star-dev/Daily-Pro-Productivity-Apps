package com.dailyapps.kitebrief.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TealMid,
    onPrimary = Color.White,
    secondary = Foam,
    onSecondary = Ink,
    background = Sand,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    tertiary = Coral
)

private val DarkColors = darkColorScheme(
    primary = Foam,
    onPrimary = TealDeep,
    secondary = TealMid,
    onSecondary = Color.White,
    background = Color(0xFF07161B),
    onBackground = Sand,
    surface = Color(0xFF0E242B),
    onSurface = Sand,
    tertiary = Coral
)

@Composable
fun KiteBriefTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
