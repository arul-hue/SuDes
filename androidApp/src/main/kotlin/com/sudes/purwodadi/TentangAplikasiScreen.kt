package com.sudes.purwodadi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TentangAplikasiScreen(
    onBackClicked: () -> Unit = {}
) {

    BackHandler {
        onBackClicked()
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GreenDesaPrimary,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tentang Aplikasi SuDes",
                        style = AppTypography.HeadingMedium.copy(color = Color.White)
                    )
                }
            }
        },
        containerColor = GrayBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo SuDes
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(GreenDesaPrimary, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "SuDes Logo",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SuDes",
                style = AppTypography.HeadingLarge,
                color = TextPrimary
            )
            Text(
                text = "Layanan Surat Online Desa",
                style = AppTypography.HeadingMedium,
                color = GreenDesaPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tag Versi
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GreenDesaLight
            ) {
                Text(
                    text = "v1.0.0",
                    style = AppTypography.Caption.copy(color = GreenDesaPrimary),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card 1: Apa itu SuDes?
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Apa itu SuDes?",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "SuDes (Surat Desa) adalah platform pelayanan publik digital untuk mempermudah warga Desa Purwodadi Simpang dalam membuat pengajuan surat kependudukan secara cepat, mandiri, dan efisien tanpa perlu antre lama di balai desa.",
                        style = AppTypography.BodyRegular,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2: Informasi Layanan
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Informasi Layanan",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Wilayah Layanan",
                        style = AppTypography.Caption,
                        color = TextSecondary
                    )
                    Text(
                        text = "Desa Purwodadi Simpang",
                        style = AppTypography.BodyRegular,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Jam Operasional Pelayanan",
                        style = AppTypography.Caption,
                        color = TextSecondary
                    )
                    Text(
                        text = "Senin - Jumat | 08.00 - 15.30 WIB",
                        style = AppTypography.BodyRegular,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Copyright
            Text(
                text = "Pemerintah Desa Purwodadi Simpang © 2026",
                style = AppTypography.Caption,
                color = TextPlaceholder,
                textAlign = TextAlign.Center
            )
        }
    }
}