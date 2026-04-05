package com.denis.vpnapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Cyan400 = Color(0xFF00BCD4)
val Cyan600 = Color(0xFF00ACC1)
val Cyan900 = Color(0xFF006064)
val Dark900 = Color(0xFF060B14)
val Dark800 = Color(0xFF0D1526)
val Dark700 = Color(0xFF121E36)
val Dark600 = Color(0xFF1A2844)
val GreenOn = Color(0xFF00E676)
val RedOff = Color(0xFFFF5252)
val TextPrimary = Color(0xFFE8F4F8)
val TextSecondary = Color(0xFF7A9BB5)

private val DarkColorScheme = darkColorScheme(
    primary = Cyan400,
    secondary = Cyan600,
    background = Dark900,
    surface = Dark800,
    onPrimary = Dark900,
    onSecondary = Dark900,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun VpnAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
