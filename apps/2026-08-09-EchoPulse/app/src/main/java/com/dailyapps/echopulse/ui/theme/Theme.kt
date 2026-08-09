package com.dailyapps.echopulse.ui.theme

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
    primary = SkyBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = DeepNavy,
    secondary = Emerald,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = Color(0xFF064E3B),
    tertiary = Violet,
    onTertiary = Color.White,
    background = Mist,
    onBackground = DeepNavy,
    surface = Color.White,
    onSurface = DeepNavy,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = SoftGray,
    error = Rose,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlueLight,
    onPrimary = DeepNavy,
    primaryContainer = SoftNavy,
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = Emerald,
    onSecondary = DeepNavy,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFD1FAE5),
    tertiary = Violet,
    onTertiary = Color.White,
    background = DeepNavy,
    onBackground = Mist,
    surface = SoftNavy,
    onSurface = Mist,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = SoftGray,
    error = Rose,
    onError = Color.White
)

@Composable
fun EchoPulseTheme(
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
