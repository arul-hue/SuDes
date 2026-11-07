package com.sudes.purwodadi

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit = {}
) {
    // Timer otomatis pindah halaman setelah 2.2 detik
    LaunchedEffect(Unit) {
        delay(2200)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenDesaPrimary),
        contentAlignment = Alignment.Center
    ) {
        // Menggunakan SpaceBetween agar posisinya pas di tengah antara logo dan copyright
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 50.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(1.dp))

            // 1. BAGIAN ATAS: Logo & Judul SuDes
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(110.dp),
                    shape = RoundedCornerShape(26.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_sudes), // Mengambil gambar dari folder drawable
                            contentDescription = "Logo SuDes",
                            modifier = Modifier.size(140.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "SuDes",
                    style = AppTypography.HeadingLarge.copy(color = Color.White)
                )
            }

            // 2. BAGIAN TENGAH: Indikator Loading & Teks
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(160.dp)
                        .height(6.dp),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Memuat layanan...",
                    style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.95f))
                )
            }

            // 3. BAGIAN BAWAH: Teks Copyright
            Text(
                text = "Pemerintah Desa Purwodadi Simpang © 2026",
                style = AppTypography.Caption.copy(color = Color.White.copy(alpha = 0.8f))
            )
        }
    }
}