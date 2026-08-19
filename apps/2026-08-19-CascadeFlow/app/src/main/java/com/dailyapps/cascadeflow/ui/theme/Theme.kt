package com.dailyapps.cascadeflow.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CascadePrimary,
    secondary = CascadeSecondary,
    tertiary = CascadeTertiary,
    background = CascadeBackground,
    surface = CascadeSurface,
    onPrimary = CascadeOnPrimary,
    onSecondary = CascadeOnPrimary,
    onTertiary = CascadeOnPrimary,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    error = CascadeError
)

private val DarkColorScheme = darkColorScheme(
    primary = CascadeDarkPrimary,
    secondary = CascadeSecondary,
    tertiary = CascadeTertiary,
    background = CascadeDarkBackground,
    surface = CascadeDarkSurface,
    onPrimary = CascadeDarkOnPrimary,
    onSecondary = CascadeDarkOnPrimary,
    onTertiary = CascadeDarkOnPrimary,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    error = CascadeError
)

@Composable
fun CascadeFlowTheme(
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
