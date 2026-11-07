package com.sudes.purwodadi

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

// Fungsi helper untuk mengambil tanggal hari ini dengan format Indonesia
fun getUsahaTodayFormatted(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
    return currentDate.format(formatter)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuratUsahaScreen(
    onBackClicked: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    // State Form Data Pelaku Usaha
    var namaLengkap by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var tempatTanggalLahir by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("") }
    var agama by remember { mutableStateOf("") }
    var statusPerkawinan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var alamatLengkap by remember { mutableStateOf("") }

    // State Detail Usaha
    var namaUsaha by remember { mutableStateOf("") }
    var lokasiUsaha by remember { mutableStateOf("") }

    // State Dialog Konfirmasi & Sukses
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    if (isSubmittedSuccess) {
        // TAMPILAN SUKSES PENGAJUAN
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GreenDesaPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Pengajuan SKU Berhasil",
                    style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 22.sp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Dokumen Usaha telah masuk ke sistem desa",
                    style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(30.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            modifier = Modifier.size(70.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = GreenDesaLight
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Success",
                                    tint = GreenDesaPrimary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Surat Keterangan Usaha (SKU)",
                            style = AppTypography.HeadingLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Pengajuan SKU Anda sedang menunggu verifikasi dari perangkat Desa Purwodadi Simpang.",
                            style = AppTypography.BodyRegular.copy(fontSize = 15.sp),
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = { /* Aksi ke halaman status */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                        ) {
                            Text(text = "Lihat Status Pengajuan", style = AppTypography.ButtonText.copy(fontSize = 16.sp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onBackClicked,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Kembali ke Beranda", color = TextPrimary, style = AppTypography.ButtonText.copy(fontSize = 16.sp))
                        }
                    }
                }
            }
        }
    } else {
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
                            text = "Surat Keterangan Usaha",
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

                // KARTU 1: DATA PELAKU USAHA
                CardSection(title = "DATA PELAKU USAHA") {
                    FormTextField(label = "Nama Lengkap", value = namaLengkap, onValueChange = { namaLengkap = it }, placeholder = "Masukkan nama lengkap")
                    FormTextField(label = "NIK", value = nik, onValueChange = { nik = it }, placeholder = "Masukkan 16 digit NIK")
                    FormTextField(label = "Tempat, Tanggal Lahir", value = tempatTanggalLahir, onValueChange = { tempatTanggalLahir = it }, placeholder = "Contoh: Bandar Lampung, 01 Januari 1990")
                    FormDropdownField(label = "Jenis Kelamin", selectedValue = jenisKelamin.ifEmpty { "Pilih jenis kelamin" })
                    FormDropdownField(label = "Agama", selectedValue = agama.ifEmpty { "Pilih agama" })
                    FormDropdownField(label = "Status Perkawinan", selectedValue = statusPerkawinan.ifEmpty { "Pilih status perkawinan" })
                    FormTextField(label = "Pekerjaan", value = pekerjaan, onValueChange = { pekerjaan = it }, placeholder = "Masukkan pekerjaan")
                    FormTextField(label = "Alamat Lengkap", value = alamatLengkap, onValueChange = { alamatLengkap = it }, placeholder = "Masukkan alamat lengkap sesuai KTP")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 2: DETAIL USAHA
                CardSection(title = "DETAIL USAHA") {
                    FormTextField(label = "Nama / Deskripsi Usaha", value = namaUsaha, onValueChange = { namaUsaha = it }, placeholder = "Contoh: Warung Sembako Berkah")
                    FormTextField(label = "Lokasi Usaha", value = lokasiUsaha, onValueChange = { lokasiUsaha = it }, placeholder = "Masukkan lokasi/alamat tempat usaha")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 3: SURAT PENGANTAR RT
                CardSection(title = "SURAT PENGANTAR RT") {
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

                // KARTU 4: INFORMASI SURAT
                CardSection(title = "INFORMASI SURAT") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Nomor Surat (Diisi Admin)", style = AppTypography.Caption.copy(fontSize = 13.sp), color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "140/ ... /VII.02.13/IX/2026", style = AppTypography.BodyRegular.copy(fontSize = 14.sp), color = TextPlaceholder)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Tanggal", style = AppTypography.Caption.copy(fontSize = 13.sp), color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = getUsahaTodayFormatted(),
                                style = AppTypography.LabelForm.copy(fontSize = 14.sp),
                                color = GreenDesaPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // TOMBOL KIRIM PENGAJUAN
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
    }

    // DIALOG KONFIRMASI KIRIM
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Color.White,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFFFEF3C7)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Kirim Pengajuan Surat?",
                        style = AppTypography.HeadingLarge.copy(fontSize = 20.sp),
                        color = TextPrimary
                    )
                }
            },
            text = {
                Text(
                    text = "Pastikan data dan dokumen pengantar RT yang Anda masukkan sudah benar dan lengkap.",
                    style = AppTypography.BodyRegular.copy(fontSize = 15.sp),
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        isSubmittedSuccess = true
                        onSubmitSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Periksa Kembali", color = TextPrimary, style = AppTypography.ButtonText.copy(fontSize = 15.sp))
                }
            }
        )
    }
}

// Komponen Pendukung Card Section
@Composable
fun CardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
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
fun FormTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
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
                focusedBorderColor = GreenDesaPrimary,
                focusedLabelColor = GreenDesaPrimary,
                cursorColor = GreenDesaPrimary
            )
        )
    }
}

// Komponen Dropdown Simulasi dengan ukuran font ramah lansia
@Composable
fun FormDropdownField(label: String, selectedValue: String) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = label, style = AppTypography.LabelForm.copy(fontSize = 15.sp), color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedButton(
            onClick = { /* Aksi Buka Pilihan Dropdown */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
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