package com.sudes.purwodadi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definisikan skema warna terang
private val LightColorScheme = lightColorScheme(
    primary = GreenDesaPrimary,        // Warna utama
    onPrimary = Color.White,           // Warna teks/ikon di atas tombol primary
    primaryContainer = GreenDesaLight,
    onPrimaryContainer = GreenDesaPrimary,
    background = GrayBackground,
    surface = Color.White,
    onSurface = TextPrimary,
    onBackground = TextPrimary
)

// Pemetaan AppTypography ke Typography standar Material 3
private val MaterialTypography = Typography(
    headlineLarge = AppTypography.HeadingLarge,
    titleMedium = AppTypography.HeadingMedium,
    bodyLarge = AppTypography.BodyRegular,
    labelLarge = AppTypography.ButtonText,
    labelMedium = AppTypography.LabelForm,
    bodySmall = AppTypography.Caption
)

@Composable
fun SudesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MaterialTypography, // Tipografi otomatis aktif di seluruh aplikasi
        content = content
    )
}