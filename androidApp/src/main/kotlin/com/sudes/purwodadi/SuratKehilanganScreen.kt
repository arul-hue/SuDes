package com.sudes.purwodadi

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudes.purwodadi.ui.theme.*

// Fungsi helper unik untuk tanggal hari ini pada Surat Kehilangan
fun getKehilanganDateFormatted(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
    return currentDate.format(formatter)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuratKehilanganScreen(
    onBackClicked: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    // State Form Data Diri Pelapor
    var namaLengkap by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var tempatTanggalLahir by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var agama by remember { mutableStateOf("") }
    var statusPerkawinan by remember { mutableStateOf("") }
    var alamatLengkap by remember { mutableStateOf("") }

    // State Detail Kehilangan
    var hariKejadian by remember { mutableStateOf("") }
    var tanggalKejadian by remember { mutableStateOf("") }
    var jamPerkiraanHilang by remember { mutableStateOf("") }
    var barangHilang by remember { mutableStateOf("") }

    // State Dialog Konfirmasi & Sukses
    var showConfirmDialog by remember { mutableStateOf(false) } // Untuk dialog "Yakin ingin kirim?"
    var isSubmittedSuccess by remember { mutableStateOf(false) }       // Untuk dialog pop-up sukses

    val scrollState = rememberScrollState()

    // TAMPILAN FORM UTAMA
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
                        text = "Surat Keterangan Kehilangan",
                        style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 20.sp)
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
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // TOMBOL ISI OTOMATIS
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = GreenDesaLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenDesaPrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚡  ", fontSize = 18.sp)
                    Text(
                        text = "Isi Otomatis dari Profil",
                        style = AppTypography.LabelForm.copy(color = GreenDesaPrimary, fontSize = 15.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 1: DATA DIRI PELAPOR[cite: 4]
            KehilanganCardSection(title = "DATA DIRI PELAPOR") {
                KehilanganFormTextField(label = "Nama Lengkap", value = namaLengkap, onValueChange = { namaLengkap = it }, placeholder = "Masukkan nama lengkap")
                KehilanganFormTextField(label = "NIK", value = nik, onValueChange = { nik = it }, placeholder = "Masukkan 16 digit NIK")
                KehilanganFormTextField(label = "Tempat, Tanggal Lahir", value = tempatTanggalLahir, onValueChange = { tempatTanggalLahir = it }, placeholder = "Contoh: Bandar Lampung, 01 Januari 1990")
                KehilanganFormTextField(label = "Pekerjaan", value = pekerjaan, onValueChange = { pekerjaan = it }, placeholder = "Masukkan pekerjaan")
                KehilanganFormDropdownField(label = "Agama", selectedValue = agama.ifEmpty { "Pilih agama" })
                KehilanganFormDropdownField(label = "Status Perkawinan", selectedValue = statusPerkawinan.ifEmpty { "Pilih status perkawinan" })
                KehilanganFormTextField(label = "Alamat Lengkap", value = alamatLengkap, onValueChange = { alamatLengkap = it }, placeholder = "Masukkan alamat sesuai KTP")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 2: DETAIL KEHILANGAN[cite: 5]
            KehilanganCardSection(title = "DETAIL KEHILANGAN") {
                KehilanganFormTextField(label = "Hari Kejadian", value = hariKejadian, onValueChange = { hariKejadian = it }, placeholder = "Contoh: Kamis")
                KehilanganFormTextField(label = "Tanggal Kejadian", value = tanggalKejadian, onValueChange = { tanggalKejadian = it }, placeholder = "Contoh: 05 November 2026")
                KehilanganFormTextField(label = "Jam Perkiraan Hilang", value = jamPerkiraanHilang, onValueChange = { jamPerkiraanHilang = it }, placeholder = "Contoh: Sekitar Pukul 10.00 WIB")
                KehilanganFormTextField(label = "Barang / Surat yang Hilang", value = barangHilang, onValueChange = { barangHilang = it }, placeholder = "Contoh: Dompet berisi KTP & STNK")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 3: SURAT PENGANTAR RT[cite: 5]
            KehilanganCardSection(title = "SURAT PENGANTAR RT") {
                OutlinedButton(
                    onClick = { /* Aksi Upload File */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = GrayBackground)
                ) {
                    Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Upload", tint = TextSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Unggah Foto/Scan Surat Pengantar RT", color = TextSecondary, style = AppTypography.BodyRegular.copy(fontSize = 15.sp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 4: INFORMASI SURAT[cite: 5]
            KehilanganCardSection(title = "INFORMASI SURAT") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Nomor Surat (Diisi Admin)", style = AppTypography.Caption.copy(fontSize = 13.sp), color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "140/ ... /LK/II/VII.02.03/2026", style = AppTypography.BodyRegular.copy(fontSize = 14.sp), color = TextPlaceholder)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Tanggal", style = AppTypography.Caption.copy(fontSize = 13.sp), color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = getKehilanganDateFormatted(),
                            style = AppTypography.LabelForm.copy(fontSize = 14.sp),
                            color = GreenDesaPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TOMBOL KIRIM PENGAJUAN[cite: 5]
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
            ) {
                Text(text = "Kirim Pengajuan Surat", style = AppTypography.ButtonText.copy(fontSize = 16.sp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // 1. POP-UP DIALOG KONFIRMASI ("Kirim Pengajuan Surat?")
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = Color(0xFFFEF3C7)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Kirim Pengajuan Surat?", style = AppTypography.HeadingLarge.copy(fontSize = 18.sp), color = TextPrimary)
                }
            },
            text = {
                Text(text = "Pastikan data dan dokumen pengantar RT yang Anda masukkan sudah benar.", style = AppTypography.BodyRegular.copy(fontSize = 14.sp), color = TextSecondary, textAlign = TextAlign.Center)
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        isSubmittedSuccess = true // Lanjut memunculkan pop-up sukses
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(text = "Ya, Kirim Sekarang", style = AppTypography.ButtonText.copy(fontSize = 15.sp))
                }

                Spacer(modifier = Modifier.height(12.dp))

            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDialog = false },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Periksa Kembali", color = TextPrimary, style = AppTypography.ButtonText.copy(fontSize = 15.sp))
                }
            }
        )
    }

    // 2. POP-UP DIALOG SUKSES ("Pengajuan Berhasil!")
    if (isSubmittedSuccess) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(modifier = Modifier.size(64.dp), shape = CircleShape, color = GreenDesaLight) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Success", tint = GreenDesaPrimary, modifier = Modifier.size(36.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Pengajuan Berhasil!", style = AppTypography.HeadingLarge.copy(fontSize = 20.sp), color = TextPrimary)
                }
            },
            text = {
                Text(text = "Dokumen Anda telah masuk ke sistem desa dan menunggu verifikasi.", style = AppTypography.BodyRegular.copy(fontSize = 14.sp), color = TextSecondary, textAlign = TextAlign.Center)
            },
            confirmButton = {
                Button(
                    onClick = {
                        isSubmittedSuccess = false
                        onSubmitSuccess() // Kembali ke Beranda / halaman sebelumnya
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(text = "Kembali ke Beranda", style = AppTypography.ButtonText.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                }
            }
        )
    }
}

// Komponen Pendukung Card Section
@Composable
fun KehilanganCardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = AppTypography.HeadingLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = BorderColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

// Komponen Input TextField dengan ukuran font ramah lansia & fokus hijau
@Composable
fun KehilanganFormTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = label, style = AppTypography.LabelForm.copy(fontSize = 15.sp), color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextPlaceholder, style = AppTypography.BodyRegular.copy(fontSize = 15.sp)) },
            textStyle = AppTypography.BodyRegular.copy(fontSize = 15.sp, color = TextPrimary),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                // Container tetap putih solid
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,

                // === GARIS BORDER ABU-ABU SAAT TIDAK FOKUS ===
                unfocusedBorderColor = GrayBorder,
                focusedBorderColor = GreenDesaPrimary, // Garis hijau saat diketik/fokus

                focusedLabelColor = GreenDesaPrimary,
                cursorColor = GreenDesaPrimary
            )
        )
    }
}

// Komponen Dropdown Simulasi dengan ukuran font ramah lansia
@Composable
fun KehilanganFormDropdownField(label: String, selectedValue: String) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = label, style = AppTypography.LabelForm.copy(fontSize = 15.sp), color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedButton(
            onClick = { /* Aksi Buka Pilihan Dropdown */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GrayBorder),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedValue, color = TextPlaceholder, style = AppTypography.BodyRegular.copy(fontSize = 15.sp))
                Text(text = "▼", color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}