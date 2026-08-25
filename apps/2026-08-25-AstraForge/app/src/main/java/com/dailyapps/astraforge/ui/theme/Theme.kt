package com.dailyapps.astraforge.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AstraPrimaryDark,
    onPrimary = Color.Black,
    secondary = AstraSecondary,
    onSecondary = Color.Black,
    tertiary = AstraAccent,
    background = AstraBackground,
    surface = AstraSurface,
    onBackground = AstraOnSurface,
    onSurface = AstraOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = AstraPrimary,
    onPrimary = Color.White,
    secondary = AstraSecondary,
    onSecondary = Color.White,
    tertiary = AstraAccent,
    background = Color(0xFFF5F3FA),
    surface = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun AstraForgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
