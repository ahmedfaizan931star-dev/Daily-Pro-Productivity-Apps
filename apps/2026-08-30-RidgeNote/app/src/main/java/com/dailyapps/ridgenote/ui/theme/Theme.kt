package com.dailyapps.ridgenote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Pine,
    onPrimary = Color.White,
    secondary = Seafoam,
    background = Sand,
    surface = Color.White,
    onBackground = Ink,
    onSurface = Ink,
    error = Clay
)

private val Dark = darkColorScheme(
    primary = Seafoam,
    onPrimary = Ink,
    secondary = Seafoam,
    background = Color(0xFF0B1C1D),
    surface = Color(0xFF132627),
    onBackground = Sand,
    onSurface = Sand,
    error = Clay
)

@Composable
fun RidgeNoteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) Dark else Light,
        typography = Typography,
        content = content
    )
}
