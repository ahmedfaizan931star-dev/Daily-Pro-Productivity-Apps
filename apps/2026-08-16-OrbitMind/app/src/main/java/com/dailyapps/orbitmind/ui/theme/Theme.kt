package com.dailyapps.orbitmind.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = OrbitPrimary,
    onPrimary = OrbitOnPrimary,
    secondary = OrbitSecondary,
    tertiary = OrbitTertiary,
    background = OrbitBackground,
    surface = OrbitSurface,
    onBackground = OrbitOnBackground,
    onSurface = OrbitOnBackground,
    error = OrbitError
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = OrbitOnPrimary,
    secondary = LightSecondary,
    tertiary = OrbitTertiary,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnBackground,
    onSurface = LightOnBackground,
    error = OrbitError
)

@Composable
fun OrbitMindTheme(
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
