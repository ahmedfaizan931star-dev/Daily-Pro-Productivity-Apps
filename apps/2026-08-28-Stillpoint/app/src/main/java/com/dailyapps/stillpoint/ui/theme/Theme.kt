package com.dailyapps.stillpoint.ui.theme

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

private val DarkColors = darkColorScheme(
    primary = Sage,
    onPrimary = Ink,
    secondary = Mist,
    onSecondary = Ink,
    tertiary = Ember,
    background = Ink,
    onBackground = Sand,
    surface = Color(0xFF10241B),
    onSurface = Sand,
    surfaceVariant = Color(0xFF1C3328)
)

private val LightColors = lightColorScheme(
    primary = Pine,
    onPrimary = Color.White,
    secondary = Moss,
    onSecondary = Color.White,
    tertiary = Sky,
    background = Sand,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Mist
)

@Composable
fun StillpointTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
