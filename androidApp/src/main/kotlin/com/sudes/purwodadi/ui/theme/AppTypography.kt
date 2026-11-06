package com.sudes.purwodadi.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ==============================================================================
// 1. WARNA UTAMA APLIKASI SUDES
// ==============================================================================
val GreenDesaPrimary = Color(0xFF1B5E20) // Hijau khas identitas desa
val GrayBackground = Color(0xFFF9FAFB)  // Latar belakang terang
val GrayBorder = Color(0xFFE5E7EB)      // Garis tepi input form

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