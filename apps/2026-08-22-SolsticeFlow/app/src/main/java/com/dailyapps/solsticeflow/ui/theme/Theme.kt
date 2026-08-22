package com.dailyapps.solsticeflow.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue,
    onPrimary = DeepNavy,
    secondary = AmberGlow,
    onSecondary = DeepNavy,
    tertiary = Mint,
    background = DeepNavy,
    onBackground = OffWhite,
    surface = SoftSlate,
    onSurface = OffWhite,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = MutedGray,
    error = Coral
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    secondary = Color(0xFFD97706),
    onSecondary = Color.White,
    tertiary = Color(0xFF059669),
    background = OffWhite,
    onBackground = DeepNavy,
    surface = Color.White,
    onSurface = DeepNavy,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    error = Coral
)

@Composable
fun SolsticeFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
