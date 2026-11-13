package com.sudes.purwodadi

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            question = "Bagaimana cara mengajukan surat online di SuDes?",
            answer = "Warga dapat masuk ke menu Layanan Surat, pilih jenis surat yang diinginkan, lengkapi formulir data diri yang tersedia, lalu klik tombol kirim. Anda dapat memantau status pengajuan langsung melalui halaman Riwayat Surat."
        ),
        FaqItem(
            question = "Apakah nomor HP wajib diisi saat pendaftaran?",
            answer = "Tidak. Berdasarkan pembaruan sistem terbaru, kolom nomor HP telah dihapus sepenuhnya. Proses verifikasi dan autentikasi kini menggunakan Akun Google atau Email & Kata Sandi."
        ),
        FaqItem(
            question = "Bagaimana cara mengganti email atau kata sandi akun?",
            answer = "Buka menu Profil, lalu pilih 'Kelola Email' atau 'Ganti Kata Sandi'. Khusus untuk akun manual (Email & Password), Anda dapat memperbarui data dengan aman melalui menu pengaturan tersebut."
        ),
        FaqItem(
            question = "Bagaimana jika email baru saya belum berubah setelah diverifikasi?",
            answer = "Jika menggunakan akun manual, setelah mengeklik tautan verifikasi di email baru Anda, kembali ke aplikasi lalu klik tombol 'Segarkan Sesi (Masuk Ulang)' untuk membersihkan cache dan menyinkronkan data terbaru dari server."
        ),
        FaqItem(
            question = "Bagaimana cara mengambil surat yang sudah disetujui?",
            answer = "Jika pengajuan surat Anda telah berstatus 'Disetujui' oleh pihak desa, warga dapat langsung datang ke Balai Desa Purwodadi Simpang dengan membawa dokumen pendukung atau menunjukkan bukti digital di aplikasi."
        ),
        FaqItem(
            question = "Apa yang harus dilakukan jika pengajuan surat ditolak?",
            answer = "Jika pengajuan ditolak, Anda akan melihat catatan alasan penolakan dari petugas desa. Perbaiki data atau berkas yang keliru pada menu detail pengajuan, lalu ajukan ulang."
        )
    )

    // State untuk melacak indeks FAQ mana saja yang sedang terbuka (bisa lebih dari satu atau satu per satu)
    // Di sini kita gunakan set agar beberapa item bisa dibuka atau ditutup independen
    var expandedItems by remember { mutableStateOf(setOf<Int>()) }

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
                        text = "Bantuan & Tanya Jawab",
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
            // Header Informasi Bantuan
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Bantuan",
                        tint = Color(0xFF1E40AF),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Butuh Bantuan Seputar Layanan Desa?",
                            style = AppTypography.HeadingMedium.copy(color = Color(0xFF1E40AF))
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tekan pada salah satu pertanyaan di bawah untuk melihat penjelasan lengkapnya.",
                            style = AppTypography.BodyRegular.copy(color = Color(0xFF1E3A8A))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Daftar Toggle List FAQ
            faqList.forEachIndexed { index, faq ->
                val isExpanded = expandedItems.contains(index)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            expandedItems = if (isExpanded) {
                                expandedItems - index
                            } else {
                                expandedItems + index
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = faq.question,
                                style = AppTypography.HeadingMedium.copy(color = TextPrimary),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Tutup" else "Buka",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Animasi buka-tutup jawaban
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = GrayBorder, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = faq.answer,
                                    style = AppTypography.BodyRegular.copy(color = TextSecondary)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}