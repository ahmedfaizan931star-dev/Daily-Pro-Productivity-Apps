package com.dailyapps.claritymatrix.ui.theme

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
    primary = MatrixCyan,
    onPrimary = Color.White,
    primaryContainer = MatrixCyanLight.copy(alpha = 0.18f),
    onPrimaryContainer = MatrixCyanDark,
    secondary = MatrixIndigo,
    onSecondary = Color.White,
    secondaryContainer = MatrixIndigoLight.copy(alpha = 0.18f),
    tertiary = MatrixEmerald,
    onTertiary = Color.White,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
    surface = CardLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    error = MatrixRose,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = MatrixCyanLight,
    onPrimary = Color(0xFF083344),
    primaryContainer = MatrixCyanDark,
    onPrimaryContainer = MatrixCyanLight,
    secondary = MatrixIndigoLight,
    onSecondary = Color(0xFF1E1B4B),
    secondaryContainer = MatrixIndigoDark,
    tertiary = MatrixEmeraldLight,
    onTertiary = Color(0xFF064E3B),
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
    surface = CardDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = MatrixRose,
    onError = Color.White
)

@Composable
fun ClarityMatrixTheme(
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
