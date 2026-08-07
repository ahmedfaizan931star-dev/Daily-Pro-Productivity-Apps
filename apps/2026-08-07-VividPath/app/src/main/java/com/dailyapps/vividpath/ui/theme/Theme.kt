package com.dailyapps.vividpath.ui.theme

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
    onPrimaryContainer = DeepSlate,
    secondary = SoftPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFCE7F3),
    onSecondaryContainer = DeepSlate,
    tertiary = Emerald,
    onTertiary = Color.White,
    background = Mist,
    onBackground = DeepSlate,
    surface = Color.White,
    onSurface = DeepSlate,
    surfaceVariant = Cloud,
    onSurfaceVariant = SoftSlate,
    error = Rose,
    outline = Color(0xFF94A3B8)
)

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlueDark,
    onPrimary = DeepSlate,
    primaryContainer = SoftSlate,
    onPrimaryContainer = SkyBlueDark,
    secondary = SoftPinkDark,
    onSecondary = DeepSlate,
    secondaryContainer = SoftSlate,
    onSecondaryContainer = SoftPinkDark,
    tertiary = EmeraldDark,
    onTertiary = DeepSlate,
    background = DeepSlate,
    onBackground = Mist,
    surface = SoftSlate,
    onSurface = Mist,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Cloud,
    error = Rose,
    outline = Color(0xFF64748B)
)

@Composable
fun VividPathTheme(
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
