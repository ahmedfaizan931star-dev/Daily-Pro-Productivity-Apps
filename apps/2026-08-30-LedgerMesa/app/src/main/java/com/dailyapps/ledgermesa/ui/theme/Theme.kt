package com.dailyapps.ledgermesa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Pine,
    secondary = Sage,
    tertiary = Clay,
    background = Cream,
    surface = Color.White
)

private val Dark = darkColorScheme(
    primary = Sage,
    secondary = Clay,
    background = Color(0xFF101814),
    surface = Color(0xFF17201B)
)

@Composable
fun LedgerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) Dark else Light,
        typography = Typography,
        content = content
    )
}
