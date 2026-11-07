package com.sudes.purwodadi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definisikan skema warna terang
private val LightColorScheme = lightColorScheme(
    primary = GreenDesaPrimary,        //  Warna utama disini
    onPrimary = Color.White,           // Warna teks/ikon di atas tombol primary
    primaryContainer = GreenDesaLight,
    onPrimaryContainer = GreenDesaPrimary,
    background = GrayBackground,
    surface = Color.White,
    onSurface = TextPrimary,
)

@Composable
fun SudesTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        // typography = AppTypography, // Sesuaikan dengan tipografi Anda
        content = content
    )
}