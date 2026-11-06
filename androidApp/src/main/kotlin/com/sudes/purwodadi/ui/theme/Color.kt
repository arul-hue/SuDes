package com.sudes.purwodadi.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ==============================================================================
// 1. WARNA UTAMA & NETRAL APLIKASI SUDES
// ==============================================================================
val GreenDesaPrimary = Color(0xFF1B5E20)       // Hijau Desa Utama
val GreenDesaLight = Color(0xFFDCFCE7)         // Hijau Muda (Background Icon / Badges)
val GreenDesaHover = Color(0xFF14532D)         // Hijau Gelap Saat State Pressed

val GrayBackground = Color(0xFFF8FAFC)         // Background Layar
val GrayBorder = Color(0xFFE5E7EB)             // Garis tepi input form
val TextPrimary = Color(0xFF1E293B)            // Warna Teks Utama / Judul
val TextSecondary = Color(0xFF64748B)          // Warna Teks Deskripsi / Subtitle
val TextPlaceholder = Color(0xFF94A3B8)        // Warna Placeholder Input
val BorderColor = Color(0xFFE2E8F0)            // Warna Outlined Border / Divider

// ==============================================================================
// 2. TIPOGRAFI RAMAH LANSIA (ACCESSIBILITY-FIRST)
// ==============================================================================
object AppTypography {
    val HeadingLarge = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold
    )

    val HeadingMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    val BodyRegular = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    )

    val LabelForm = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )

    val ButtonText = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    val Caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
}