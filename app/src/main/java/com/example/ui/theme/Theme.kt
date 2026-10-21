package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ClipForgeDarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = Color(0xFF001F24),
    primaryContainer = Color(0xFF00363D),
    onPrimaryContainer = ElectricCyan,

    secondary = FieryOrange,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF4A1E00),
    onSecondaryContainer = FieryOrange,

    tertiary = NeonPurple,
    onTertiary = Color(0xFF1E0038),
    tertiaryContainer = Color(0xFF3B1261),
    onTertiaryContainer = NeonPurple,

    background = DarkVoidBg,
    onBackground = TextPrimary,

    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    error = CrimsonAlert,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Enforce our custom premium gaming palette
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ClipForgeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
