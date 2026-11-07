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

// Fungsi helper unik untuk tanggal hari ini pada Surat Kematian
fun getKematianDateFormatted(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
    return currentDate.format(formatter)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuratKematianScreen(
    onBackClicked: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    // State Data Diri Almarhum / Almarhumah[cite: 6]
    var namaAlmarhum by remember { mutableStateOf("") }
    var nikAlmarhum by remember { mutableStateOf("") }
    var umur by remember { mutableStateOf("") }
    var tempatTanggalLahir by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("") }
    var agama by remember { mutableStateOf("") }
    var statusPerkawinan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var alamatAlmarhum by remember { mutableStateOf("") }

    // State Detail Kematian (Waktu & Tempat)[cite: 8]
    var pukulWafat by remember { mutableStateOf("") }
    var hariWafat by remember { mutableStateOf("") }
    var tanggalWafat by remember { mutableStateOf("") }
    var tempatWafat by remember { mutableStateOf("") }

    // State Detail Pemakaman[cite: 8]
    var pukulPemakaman by remember { mutableStateOf("") }
    var hariPemakaman by remember { mutableStateOf("") }
    var tanggalPemakaman by remember { mutableStateOf("") }
    var tempatPemakaman by remember { mutableStateOf("") }

    // State Data Pelapor[cite: 7]
    var namaPelapor by remember { mutableStateOf("") }
    var nikPelapor by remember { mutableStateOf("") }
    var alamatPelapor by remember { mutableStateOf("") }
    var hubunganPelapor by remember { mutableStateOf("") }

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
                    text = "Pengajuan Berhasil",
                    style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 22.sp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Dokumen Kematian telah masuk ke sistem desa",
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
                            text = "Surat Keterangan Kematian",
                            style = AppTypography.HeadingLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Pengajuan Surat Keterangan Kematian Anda sedang menunggu verifikasi dari perangkat Desa Purwodadi Simpang.",
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
                            text = "Surat Keterangan Kematian",
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

                // KARTU 1: DATA DIRI ALMARHUM / ALMARHUMAH[cite: 6]
                KematianCardSection(title = "DATA DIRI ALMARHUM / ALMARHUMAH") {
                    KematianFormTextField(label = "Nama Lengkap", value = namaAlmarhum, onValueChange = { namaAlmarhum = it }, placeholder = "Masukkan nama almarhum/almarhumah")
                    KematianFormTextField(label = "NIK", value = nikAlmarhum, onValueChange = { nikAlmarhum = it }, placeholder = "Masukkan 16 digit NIK")
                    KematianFormTextField(label = "Umur", value = umur, onValueChange = { umur = it }, placeholder = "Contoh: 65 Tahun")
                    KematianFormTextField(label = "Tempat, Tanggal Lahir", value = tempatTanggalLahir, onValueChange = { tempatTanggalLahir = it }, placeholder = "Contoh: Bandar Lampung, 01 Januari 1961")
                    KematianFormDropdownField(label = "Jenis Kelamin", selectedValue = jenisKelamin.ifEmpty { "Pilih jenis kelamin" })
                    KematianFormDropdownField(label = "Agama", selectedValue = agama.ifEmpty { "Pilih agama" })
                    KematianFormDropdownField(label = "Status Perkawinan", selectedValue = statusPerkawinan.ifEmpty { "Pilih status perkawinan" })
                    KematianFormTextField(label = "Pekerjaan", value = pekerjaan, onValueChange = { pekerjaan = it }, placeholder = "Masukkan pekerjaan terakhir")
                    KematianFormTextField(label = "Alamat Lengkap", value = alamatAlmarhum, onValueChange = { alamatAlmarhum = it }, placeholder = "Masukkan alamat almarhum")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 2: DETAIL KEMATIAN (WAKTU & TEMPAT)[cite: 8]
                KematianCardSection(title = "DETAIL KEMATIAN (WAKTU & TEMPAT)") {
                    KematianFormTextField(label = "Pukul / Jam Wafat", value = pukulWafat, onValueChange = { pukulWafat = it }, placeholder = "Contoh: 08.00 WIB")
                    KematianFormTextField(label = "Hari Wafat", value = hariWafat, onValueChange = { hariWafat = it }, placeholder = "Contoh: Kamis")
                    KematianFormTextField(label = "Tanggal Wafat", value = tanggalWafat, onValueChange = { tanggalWafat = it }, placeholder = "Contoh: 05 November 2026")
                    KematianFormTextField(label = "Tempat Wafat", value = tempatWafat, onValueChange = { tempatWafat = it }, placeholder = "Contoh: Rumah Kediaman / Rumah Sakit")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 3: DETAIL PEMAKAMAN[cite: 8]
                KematianCardSection(title = "DETAIL PEMAKAMAN") {
                    KematianFormTextField(label = "Pukul Pemakaman", value = pukulPemakaman, onValueChange = { pukulPemakaman = it }, placeholder = "Contoh: 14.00 WIB")
                    KematianFormTextField(label = "Hari Pemakaman", value = hariPemakaman, onValueChange = { hariPemakaman = it }, placeholder = "Contoh: Kamis")
                    KematianFormTextField(label = "Tanggal Pemakaman", value = tanggalPemakaman, onValueChange = { tanggalPemakaman = it }, placeholder = "Contoh: 05 November 2026")
                    KematianFormTextField(label = "Tempat Pemakaman (TPU)", value = tempatPemakaman, onValueChange = { tempatPemakaman = it }, placeholder = "Contoh: TPU Desa Purwodadi Simpang")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 4: DATA PELAPOR[cite: 7]
                KematianCardSection(title = "DATA PELAPOR") {
                    KematianFormTextField(label = "Nama Pelapor", value = namaPelapor, onValueChange = { namaPelapor = it }, placeholder = "Masukkan nama pelapor")
                    KematianFormTextField(label = "NIK Pelapor", value = nikPelapor, onValueChange = { nikPelapor = it }, placeholder = "Masukkan 16 digit NIK pelapor")
                    KematianFormTextField(label = "Alamat Pelapor", value = alamatPelapor, onValueChange = { alamatPelapor = it }, placeholder = "Masukkan alamat lengkap pelapor")
                    KematianFormTextField(label = "Status / Hubungan dengan Almarhum", value = hubunganPelapor, onValueChange = { hubunganPelapor = it }, placeholder = "Contoh: Anak / Suami / Istri")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 5: SURAT PENGANTAR RT[cite: 7]
                KematianCardSection(title = "SURAT PENGANTAR RT") {
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

                // KARTU 6: INFORMASI SURAT[cite: 7]
                KematianCardSection(title = "INFORMASI SURAT") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Nomor Surat (Diisi Admin)", style = AppTypography.Caption.copy(fontSize = 13.sp), color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "470/ ... /IX/SKM/VII.02.03/2026", style = AppTypography.BodyRegular.copy(fontSize = 14.sp), color = TextPlaceholder)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Tanggal", style = AppTypography.Caption.copy(fontSize = 13.sp), color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = getKematianDateFormatted(),
                                style = AppTypography.LabelForm.copy(fontSize = 14.sp),
                                color = GreenDesaPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // TOMBOL KIRIM PENGAJUAN[cite: 7]
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
fun KematianCardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
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
fun KematianFormTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
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
fun KematianFormDropdownField(label: String, selectedValue: String) {
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