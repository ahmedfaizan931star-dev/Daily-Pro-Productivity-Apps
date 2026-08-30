package com.dailyapps.quilldeck.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Ink,
    secondary = Leaf,
    tertiary = Gold,
    background = Paper,
    surface = Color.White
)

private val Dark = darkColorScheme(
    primary = Color(0xFF9FD0B8),
    secondary = Color(0xFF7CB89A),
    tertiary = Gold,
    background = Color(0xFF101814),
    surface = Color(0xFF18241E)
)

@Composable
fun QuillDeckTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (dark) Dark else Light,
        typography = Typography,
        content = content
    )
}
