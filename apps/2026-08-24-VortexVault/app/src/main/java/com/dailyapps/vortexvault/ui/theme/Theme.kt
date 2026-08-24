package com.dailyapps.vortexvault.ui.theme

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
    primary = VortexPrimaryDark,
    onPrimary = Color.Black,
    secondary = VortexSecondary,
    onSecondary = Color.Black,
    tertiary = VortexAccent,
    background = VortexBackground,
    surface = VortexSurface,
    onBackground = VortexOnSurface,
    onSurface = VortexOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = VortexPrimary,
    onPrimary = Color.White,
    secondary = VortexSecondary,
    onSecondary = Color.White,
    tertiary = VortexAccent,
    background = Color(0xFFF8F7FC),
    surface = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun VortexVaultTheme(
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
