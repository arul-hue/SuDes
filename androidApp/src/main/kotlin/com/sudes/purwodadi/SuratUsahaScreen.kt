package com.sudes.purwodadi

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.sudes.purwodadi.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Fungsi helper aman (kompatibel dengan API 24 ke atas) untuk format tanggal Indonesia
fun getUsahaTodayFormatted(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
    return sdf.format(Date())
}

// Fungsi helper pembuat file sementara untuk kamera
fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuratUsahaScreen(
    initialNama: String = "",
    initialNik: String = "",
    initialTempatTgl: String = "",
    initialAgama: String = "",
    initialStatusPerkawinan: String = "",
    initialPekerjaan: String = "",
    initialAlamat: String = "",
    onBackClicked: () -> Unit = {},
    onSubmitPengajuan: (PengajuanItem) -> Unit = {}
) {
    val context = LocalContext.current

    // State Form Data Pelaku Usaha
    var namaLengkap by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var tempatTanggalLahir by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("") }
    var agama by remember { mutableStateOf("") }
    var statusPerkawinan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var alamatLengkap by remember { mutableStateOf("") }

    // State untuk toggle tombol Isi Otomatis dari Profil
    var isAutoFilled by remember { mutableStateOf(false) }

    // State Detail Usaha
    var namaUsaha by remember { mutableStateOf("") }
    var lokasiUsaha by remember { mutableStateOf("") }

    // State untuk Lampiran Berkas / Foto Surat Pengantar RT
    var pengantarUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // State untuk Dialog Pilihan Sumber Gambar (Kamera vs Galeri) & Preview Gambar
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var showImagePreviewDialog by remember { mutableStateOf(false) }

    // Launcher Galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            pengantarUri = uri
        }
    }

    // Launcher Kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            pengantarUri = tempCameraUri
        }
    }

    // Launcher Permission Kamera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val photoFile = createImageFile(context)
            val photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            tempCameraUri = photoUri
            cameraLauncher.launch(photoUri)
        }
    }

    // State Validasi Error & Dialog
    var showErrorValidation by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    BackHandler {
        when {
            isSubmittedSuccess -> {
                isSubmittedSuccess = false
                onSubmitPengajuan(
                    createPengajuanItem(
                        nama = namaLengkap,
                        nik = nik,
                        ttl = tempatTanggalLahir,
                        jk = jenisKelamin,
                        agama = agama,
                        pekerjaan = pekerjaan,
                        alamat = alamatLengkap,
                        namaUsaha = namaUsaha,
                        lokasiUsaha = lokasiUsaha,
                        uri = pengantarUri
                    )
                )
            }
            showConfirmDialog -> {
                showConfirmDialog = false
            }
            else -> {
                onBackClicked()
            }
        }
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
                        text = "Surat Keterangan Usaha",
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
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // TOMBOL ISI OTOMATIS DARI PROFIL
            Surface(
                onClick = {
                    isAutoFilled = !isAutoFilled
                    if (isAutoFilled) {
                        namaLengkap = initialNama.takeIf { it != "-" && it.isNotBlank() } ?: ""
                        nik = initialNik.takeIf { it != "-" && it.isNotBlank() } ?: ""
                        tempatTanggalLahir = initialTempatTgl.takeIf { it != "-" && it.isNotBlank() } ?: ""
                        jenisKelamin = ""
                        agama = initialAgama.takeIf { it != "-" && it.isNotBlank() } ?: ""
                        statusPerkawinan = initialStatusPerkawinan.takeIf { it != "-" && it.isNotBlank() } ?: ""
                        pekerjaan = initialPekerjaan.takeIf { it != "-" && it.isNotBlank() } ?: ""
                        alamatLengkap = initialAlamat.takeIf { it != "-" && it.isNotBlank() } ?: ""
                    } else {
                        namaLengkap = ""
                        nik = ""
                        tempatTanggalLahir = ""
                        jenisKelamin = ""
                        agama = ""
                        statusPerkawinan = ""
                        pekerjaan = ""
                        alamatLengkap = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (isAutoFilled) GreenDesaPrimary else GreenDesaLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenDesaPrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (isAutoFilled) "✓  " else "⚡  ", style = AppTypography.HeadingMedium)
                    Text(
                        text = if (isAutoFilled) "Data Terisi Otomatis dari Profil" else "Isi Otomatis dari Profil",
                        style = AppTypography.LabelForm.copy(
                            color = if (isAutoFilled) Color.White else GreenDesaPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 1: DATA PELAKU USAHA
            CardSection(title = "DATA PELAKU USAHA") {
                FormTextField(
                    label = "Nama Lengkap",
                    value = namaLengkap,
                    onValueChange = { namaLengkap = it },
                    placeholder = "Masukkan nama lengkap",
                    isError = showErrorValidation && namaLengkap.isBlank()
                )
                FormTextField(
                    label = "NIK",
                    value = nik,
                    onValueChange = { nik = it },
                    placeholder = "Masukkan 16 digit NIK",
                    isError = showErrorValidation && nik.isBlank()
                )
                FormTextField(
                    label = "Tempat, Tanggal Lahir",
                    value = tempatTanggalLahir,
                    onValueChange = { tempatTanggalLahir = it },
                    placeholder = "Contoh: Bandar Lampung, 01 Januari 1990",
                    isError = showErrorValidation && tempatTanggalLahir.isBlank()
                )

                SuratUsahaDropdownField(
                    label = "Jenis Kelamin",
                    selectedValue = jenisKelamin,
                    options = listOf("Laki-laki", "Perempuan"),
                    onOptionSelected = { jenisKelamin = it },
                    isError = showErrorValidation && jenisKelamin.isBlank()
                )

                SuratUsahaDropdownField(
                    label = "Agama",
                    selectedValue = agama,
                    options = listOf("Islam", "Kristen Protestan", "Kristen Katolik", "Hindu", "Buddha", "Konghucu"),
                    onOptionSelected = { agama = it },
                    isError = showErrorValidation && agama.isBlank()
                )

                SuratUsahaDropdownField(
                    label = "Status Perkawinan",
                    selectedValue = statusPerkawinan,
                    options = listOf("Belum Kawin", "Kawin", "Cerai Hidup", "Cerai Mati"),
                    onOptionSelected = { statusPerkawinan = it },
                    isError = showErrorValidation && statusPerkawinan.isBlank()
                )

                FormTextField(
                    label = "Pekerjaan",
                    value = pekerjaan,
                    onValueChange = { pekerjaan = it },
                    placeholder = "Masukkan pekerjaan",
                    isError = showErrorValidation && pekerjaan.isBlank()
                )

                FormTextField(
                    label = "Alamat Lengkap",
                    value = alamatLengkap,
                    onValueChange = { alamatLengkap = it },
                    placeholder = "Masukkan alamat lengkap sesuai KTP",
                    singleLine = false,
                    minLines = 3,
                    maxLines = 4,
                    isError = showErrorValidation && alamatLengkap.isBlank()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 2: DETAIL USAHA
            CardSection(title = "DETAIL USAHA") {
                FormTextField(
                    label = "Nama / Deskripsi Usaha",
                    value = namaUsaha,
                    onValueChange = { namaUsaha = it },
                    placeholder = "Contoh: Warung Sembako Berkah",
                    isError = showErrorValidation && namaUsaha.isBlank()
                )

                FormTextField(
                    label = "Lokasi Usaha",
                    value = lokasiUsaha,
                    onValueChange = { lokasiUsaha = it },
                    placeholder = "Masukkan lokasi/alamat tempat usaha",
                    singleLine = false,
                    minLines = 2,
                    maxLines = 3,
                    isError = showErrorValidation && lokasiUsaha.isBlank()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 3: SURAT PENGANTAR RT
            CardSection(title = "SURAT PENGANTAR RT") {
                OutlinedButton(
                    onClick = { showImageSourceDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (showErrorValidation && pengantarUri == null) Color(0xFFFEF2F2) else GrayBackground
                    ),
                    border = if (showErrorValidation && pengantarUri == null) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626)) else null
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Upload",
                        tint = if (pengantarUri != null) GreenDesaPrimary else if (showErrorValidation) Color(0xFFDC2626) else TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (pengantarUri != null) "Ubah Berkas / Foto Pengantar" else "Unggah atau Foto Surat Pengantar RT",
                        color = if (pengantarUri != null) GreenDesaPrimary else if (showErrorValidation) Color(0xFFDC2626) else TextSecondary,
                        style = AppTypography.BodyRegular
                    )
                }

                // PRATINJAU BERKAS & TOMBOL LIHAT/HAPUS
                if (pengantarUri != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = GreenDesaLight.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GreenDesaPrimary.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = GreenDesaPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pengantar RT Terlampir",
                                    style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.Medium),
                                    color = GreenDesaPrimary
                                )
                            }
                            Row {
                                TextButton(onClick = { showImagePreviewDialog = true }) {
                                    Text(text = "Lihat", color = GreenDesaPrimary, style = AppTypography.LabelForm)
                                }
                                TextButton(onClick = { pengantarUri = null }) {
                                    Text(text = "Hapus", color = Color(0xFFDC2626), style = AppTypography.Caption)
                                }
                            }
                        }
                    }
                }

                if (showErrorValidation && pengantarUri == null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lampiran surat pengantar RT wajib dilampirkan",
                        style = AppTypography.Caption.copy(color = Color(0xFFDC2626))
                    )
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
                        Text(text = "Nomor Surat (Diisi Admin)", style = AppTypography.Caption, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "-", style = AppTypography.BodyRegular, color = TextPlaceholder)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Tanggal Pengajuan", style = AppTypography.Caption, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = getUsahaTodayFormatted(),
                            style = AppTypography.LabelForm,
                            color = GreenDesaPrimary
                        )
                    }
                }
            }

            // PESAN PERINGATAN GLOBAL JIKA ADA YANG KOSONG
            if (showErrorValidation && (namaLengkap.isBlank() || nik.isBlank() || tempatTanggalLahir.isBlank() || jenisKelamin.isBlank() || agama.isBlank() || statusPerkawinan.isBlank() || pekerjaan.isBlank() || alamatLengkap.isBlank() || namaUsaha.isBlank() || lokasiUsaha.isBlank() || pengantarUri == null)) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFEF2F2)
                ) {
                    Text(
                        text = "⚠️ Mohon lengkapi seluruh data formulir dan surat pengantar RT yang bertanda merah.",
                        style = AppTypography.BodyRegular.copy(color = Color(0xFFDC2626), fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TOMBOL KIRIM PENGAJUAN
            Button(
                onClick = {
                    val isFormValid = namaLengkap.isNotBlank() &&
                            nik.isNotBlank() &&
                            tempatTanggalLahir.isNotBlank() &&
                            jenisKelamin.isNotBlank() &&
                            agama.isNotBlank() &&
                            statusPerkawinan.isNotBlank() &&
                            pekerjaan.isNotBlank() &&
                            alamatLengkap.isNotBlank() &&
                            namaUsaha.isNotBlank() &&
                            lokasiUsaha.isNotBlank() &&
                            pengantarUri != null

                    if (isFormValid) {
                        showErrorValidation = false
                        showConfirmDialog = true
                    } else {
                        showErrorValidation = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
            ) {
                Text(text = "Kirim Pengajuan Surat", style = AppTypography.ButtonText)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // 0. DIALOG PILIHAN SUMBER GAMBAR (KAMERA / GALERI)
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Pilih Sumber Berkas",
                    style = AppTypography.HeadingMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = {
                            showImageSourceDialog = false
                            val permissionCheck = ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            )
                            if (permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                                val photoFile = createImageFile(context)
                                val photoUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    photoFile
                                )
                                tempCameraUri = photoUri
                                cameraLauncher.launch(photoUri)
                            } else {
                                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GreenDesaPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Ambil Foto Langsung", color = TextPrimary, style = AppTypography.ButtonText)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            showImageSourceDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Icon(Icons.Default.AttachFile, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Pilih dari Galeri", style = AppTypography.ButtonText)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    // 0.1 DIALOG PREVIEW / LIHAT FOTO (DENGAN TAMPILAN GAMBAR ASLI)
    if (showImagePreviewDialog && pengantarUri != null) {
        AlertDialog(
            onDismissRequest = { showImagePreviewDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Pratinjau Surat Pengantar RT",
                    style = AppTypography.HeadingMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = GrayBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GrayBorder)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            coil.compose.AsyncImage(
                                model = pengantarUri,
                                contentDescription = "Pratinjau Surat Pengantar RT",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showImagePreviewDialog = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(text = "Tutup", style = AppTypography.ButtonText)
                }
            },
            dismissButton = {}
        )
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
                            Text(text = "!", style = AppTypography.HeadingLarge, color = Color(0xFFD97706))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Kirim Pengajuan Surat?", style = AppTypography.HeadingMedium, color = TextPrimary)
                }
            },
            text = {
                Text(text = "Pastikan data dan dokumen pengantar RT yang Anda masukkan sudah benar.", style = AppTypography.BodyRegular, color = TextSecondary, textAlign = TextAlign.Center)
            },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            showConfirmDialog = false
                            isSubmittedSuccess = true
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(text = "Ya, Kirim Sekarang", style = AppTypography.ButtonText)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { showConfirmDialog = false },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Periksa Kembali", color = TextPrimary, style = AppTypography.ButtonText)
                    }
                }
            },
            dismissButton = {}
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
                    Text(text = "Pengajuan Berhasil!", style = AppTypography.HeadingMedium, color = TextPrimary)
                }
            },
            text = {
                Text(text = "Dokumen Anda telah masuk ke sistem desa dan menunggu verifikasi admin.", style = AppTypography.BodyRegular, color = TextSecondary, textAlign = TextAlign.Center)
            },
            confirmButton = {
                Button(
                    onClick = {
                        isSubmittedSuccess = false
                        onSubmitPengajuan(
                            createPengajuanItem(
                                nama = namaLengkap,
                                nik = nik,
                                ttl = tempatTanggalLahir,
                                jk = jenisKelamin,
                                agama = agama,
                                pekerjaan = pekerjaan,
                                alamat = alamatLengkap,
                                namaUsaha = namaUsaha,
                                lokasiUsaha = lokasiUsaha,
                                uri = pengantarUri
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(text = "Lihat Status Pengajuan", style = AppTypography.ButtonText)
                }
            }
        )
    }
}

// Fungsi bantu pembuat objek PengajuanItem
private fun createPengajuanItem(
    nama: String,
    nik: String,
    ttl: String,
    jk: String,
    agama: String,
    pekerjaan: String,
    alamat: String,
    namaUsaha: String,
    lokasiUsaha: String,
    uri: Uri?
): PengajuanItem {
    return PengajuanItem(
        id = System.currentTimeMillis().toString(),
        jenisSurat = "Surat Keterangan Usaha",
        namaPemohon = nama.ifBlank { "Warga Pemohon" },
        tanggal = getUsahaTodayFormatted(),
        status = StatusPengajuan.PROSES,
        pesanStatus = "Berkas sedang dicek oleh perangkat desa",
        nik = nik,
        tempatTanggalLahir = ttl,
        jenisKelamin = jk.ifBlank { "Laki-laki" },
        agama = agama,
        pekerjaan = pekerjaan,
        alamatLengkap = alamat,
        namaUsaha = namaUsaha,
        lokasiUsaha = lokasiUsaha,
        pengantarUri = uri?.toString() ?: "",
        nomorSurat = "-"
    )
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
                style = AppTypography.HeadingMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = BorderColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

// Komponen Input TextField Standar dengan dukungan Error Merah
@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false
) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = AppTypography.LabelForm, color = if (isError) Color(0xFFDC2626) else TextPrimary)
            if (isError) {
                Text(text = "Wajib diisi", style = AppTypography.Caption.copy(color = Color(0xFFDC2626)))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextPlaceholder, style = AppTypography.BodyRegular) },
            textStyle = AppTypography.BodyRegular.copy(color = TextPrimary),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                unfocusedBorderColor = if (isError) Color(0xFFDC2626) else GrayBorder,
                focusedBorderColor = if (isError) Color(0xFFDC2626) else GreenDesaPrimary,
                focusedLabelColor = GreenDesaPrimary,
                cursorColor = GreenDesaPrimary,
                errorBorderColor = Color(0xFFDC2626)
            )
        )
    }
}

// Komponen Dropdown Interaktif Khusus Surat Usaha dengan dukungan Error Merah
@Composable
fun SuratUsahaDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    isError: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = AppTypography.LabelForm, color = if (isError) Color(0xFFDC2626) else TextPrimary)
            if (isError) {
                Text(text = "Wajib dipilih", style = AppTypography.Caption.copy(color = Color(0xFFDC2626)))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isError) Color(0xFFDC2626) else GrayBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isError) Color(0xFFFEF2F2) else Color.White
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedValue.isBlank()) "Pilih $label" else selectedValue,
                        color = if (selectedValue.isBlank()) TextPlaceholder else TextPrimary,
                        style = AppTypography.BodyRegular
                    )
                    Text(
                        text = if (expanded) "▲" else "▼",
                        color = if (isError) Color(0xFFDC2626) else GreenDesaPrimary,
                        style = AppTypography.BodyRegular
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White),
                offset = androidx.compose.ui.unit.DpOffset(0.dp, 4.dp)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp),
                        text = {
                            Text(
                                text = option,
                                style = AppTypography.BodyRegular.copy(
                                    fontWeight = if (option == selectedValue) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (option == selectedValue) GreenDesaPrimary else TextPrimary
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}