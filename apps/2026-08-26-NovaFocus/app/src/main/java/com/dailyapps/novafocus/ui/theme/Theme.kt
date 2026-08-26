package com.dailyapps.novafocus.ui.theme

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
    primary = NovaPrimaryDark,
    onPrimary = Color.Black,
    secondary = NovaSecondary,
    onSecondary = Color.Black,
    tertiary = Pink80,
    background = NovaBackground,
    onBackground = Color.White,
    surface = NovaSurface,
    onSurface = Color.White,
    surfaceVariant = NovaCard,
    onSurfaceVariant = Color(0xFFB0B0C0),
    error = NovaError
)

private val LightColorScheme = lightColorScheme(
    primary = NovaPrimary,
    onPrimary = NovaOnPrimary,
    secondary = NovaSecondary,
    onSecondary = Color.White,
    tertiary = Pink40,
    background = Color(0xFFF8F7FC),
    onBackground = Color(0xFF1A1B2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1B2E),
    surfaceVariant = Color(0xFFF0EEF8),
    onSurfaceVariant = Color(0xFF5A5A6E),
    error = NovaError
)

@Composable
fun NovaFocusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
