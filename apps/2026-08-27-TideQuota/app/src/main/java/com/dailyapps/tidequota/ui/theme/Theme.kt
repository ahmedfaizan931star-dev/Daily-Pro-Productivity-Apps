package com.dailyapps.tidequota.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TideAqua,
    onPrimary = Color.White,
    secondary = TideFoam,
    onSecondary = TideInk,
    tertiary = TideSand,
    background = TideMist,
    onBackground = TideInk,
    surface = Color.White,
    onSurface = TideInk,
    error = TideCoral
)

private val DarkColors = darkColorScheme(
    primary = TideFoam,
    onPrimary = TideInk,
    secondary = TideAqua,
    onSecondary = Color.White,
    tertiary = TideSand,
    background = TideInk,
    onBackground = TideMist,
    surface = TideSurfaceDark,
    onSurface = TideMist,
    error = TideCoral
)

@Composable
fun TideQuotaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
