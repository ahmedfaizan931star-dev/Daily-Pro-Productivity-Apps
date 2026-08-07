package com.dailyapps.pulseforge.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = PulseIndigo,
    onPrimary = Color.White,
    primaryContainer = PulseIndigoLight.copy(alpha = 0.2f),
    onPrimaryContainer = PulseIndigoDark,
    secondary = PulseViolet,
    onSecondary = Color.White,
    secondaryContainer = PulseVioletLight.copy(alpha = 0.2f),
    tertiary = PulseCyan,
    onTertiary = Color.White,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
    surface = CardLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    error = PulseRose,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = PulseIndigoLight,
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = PulseIndigoDark,
    onPrimaryContainer = PulseIndigoLight,
    secondary = PulseVioletLight,
    onSecondary = Color(0xFF2E1065),
    secondaryContainer = Color(0xFF4C1D95),
    tertiary = PulseCyan,
    onTertiary = Color(0xFF083344),
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
    surface = CardDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = PulseRose,
    onError = Color.White
)

@Composable
fun PulseForgeTheme(
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
