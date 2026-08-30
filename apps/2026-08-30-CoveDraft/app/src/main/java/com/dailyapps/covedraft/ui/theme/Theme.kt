package com.dailyapps.covedraft.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Tide,
    onPrimary = Color.White,
    secondary = Deep,
    background = Foam,
    surface = Color.White,
    error = Ember
)

private val Dark = darkColorScheme(
    primary = Tide,
    onPrimary = Color.White,
    secondary = Sand,
    background = Color(0xFF0B1F20),
    surface = Color(0xFF123233),
    error = Ember
)

@Composable
fun CoveDraftTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) Dark else Light,
        typography = Typography,
        content = content
    )
}
