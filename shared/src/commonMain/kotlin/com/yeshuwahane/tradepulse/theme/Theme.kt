package com.yeshuwahane.tradepulse.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E676),
    onPrimary = Color(0xFF003716),
    primaryContainer = Color(0xFF005224),
    onPrimaryContainer = Color(0xFFB9FFC9),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF0B3912),
    secondaryContainer = Color(0xFF2E7D32),
    onSecondaryContainer = Color(0xFFE8F5E9),
    tertiary = Color(0xFF26A69A),
    onTertiary = Color(0xFF003731),
    tertiaryContainer = Color(0xFF004D40),
    onTertiaryContainer = Color(0xFF80CBC4),
    background = Color(0xFF0C100E),
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF141A17),
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = Color(0xFF202622),
    onSurfaceVariant = Color(0xFFC0C9C2),
    outline = Color(0xFF8B938D),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0F9D58),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC6F4D6),
    onPrimaryContainer = Color(0xFF003816),
    secondary = Color(0xFF388E3C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = Color(0xFF0B3912),
    tertiary = Color(0xFF00796B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE0F2F1),
    onTertiaryContainer = Color(0xFF004D40),
    background = Color(0xFFF6FAF8),
    onBackground = Color(0xFF1A1C1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1A),
    surfaceVariant = Color(0xFFDFE4DF),
    onSurfaceVariant = Color(0xFF424943),
    outline = Color(0xFF737973),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

@Composable
fun TradePulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
