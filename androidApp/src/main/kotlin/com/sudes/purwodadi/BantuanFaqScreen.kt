package com.sudes.purwodadi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

data class FaqItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BantuanFaqScreen(
    onBackClicked: () -> Unit = {}
) {
    val faqList = listOf(
        FaqItem(
            "Mengapa pengajuan surat saya ditolak?",
            "Biasanya karena foto surat pengantar RT kurang jelas, NIK salah, atau syarat berkas pendukung belum lengkap."
        ),
        FaqItem(
            "Bagaimana cara agar data diri otomatis terisi?",
            "Anda cukup mengisi lengkap menu \"Data Diri\" di halaman Profil dan menekan tombol Simpan terlebih dahulu."
        ),
        FaqItem(
            "Di mana saya bisa mengambil surat?",
            "Surat yang berstatus \"Diterima\" dapat diambil langsung di Balai Desa Purwodadi Simpang pada jam kerja."
        ),
        FaqItem(
            "Bagaimana jika saya lupa password akun?",
            "Gunakan fitur \"Lupa Password\" di halaman login, lalu periksa email Anda untuk tautan reset kata sandi."
        ),
        FaqItem(
            "Berapa lama proses verifikasi surat?",
            "Proses verifikasi biasanya membutuhkan waktu 1 × 24 jam pada hari kerja aktif."
        )
    )

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
                        text = "Bantuan & Pertanyaan Umum",
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
                .padding(16.dp)
        ) {
            Text(
                text = "Pertanyaan yang sering ditanyakan oleh warga:",
                style = AppTypography.BodyRegular,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            faqList.forEach { faq ->
                FaqCardItem(faq = faq)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FaqCardItem(faq: FaqItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = faq.question,
                style = AppTypography.HeadingMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = faq.answer,
                style = AppTypography.BodyRegular,
                color = TextSecondary
            )
        }
    }
}