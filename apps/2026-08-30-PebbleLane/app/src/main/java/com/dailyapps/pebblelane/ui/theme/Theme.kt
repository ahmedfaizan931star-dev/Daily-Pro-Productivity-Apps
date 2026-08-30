package com.dailyapps.pebblelane.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Tide,
    secondary = Foam,
    tertiary = Sand,
    background = Color(0xFFF6F1EA),
    surface = Color(0xFFFFFBFF)
)
private val Dark = darkColorScheme(
    primary = Foam,
    secondary = Sand,
    tertiary = Tide,
    background = Stone,
    surface = Color(0xFF1A242E)
)

@Composable
fun PebbleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) Dark else Light,
        typography = Typography,
        content = content
    )
}
