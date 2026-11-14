package com.sudes.purwodadi

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@Composable
fun AdminEditPengajuanScreen(
    pengajuan: PengajuanItem, // 👈 Menerima objek PengajuanItem asli dari inputan warga
    onBackClicked: () -> Unit = {},
    onSaveSuccess: (updatedItem: PengajuanItem) -> Unit = {}
) {
    // Inisialisasi state form langsung dari data real yang diinput warga
    var nama by remember { mutableStateOf(pengajuan.namaPemohon) }
    var nik by remember { mutableStateOf(pengajuan.nik) }
    var ttl by remember { mutableStateOf(pengajuan.tempatTanggalLahir) }
    var jenisKelamin by remember { mutableStateOf(pengajuan.jenisKelamin) }
    var agama by remember { mutableStateOf(pengajuan.agama) }
    var statusPerkawinan by remember { mutableStateOf(pengajuan.statusPerkawinan) }
    var pekerjaan by remember { mutableStateOf(pengajuan.pekerjaan) }
    var alamatLengkap by remember { mutableStateOf(pengajuan.alamatLengkap) }

    // Field khusus Detail Usaha (jika jenis surat adalah Surat Keterangan Usaha)
    var namaUsaha by remember { mutableStateOf(pengajuan.namaUsaha) }
    var lokasiUsaha by remember { mutableStateOf(pengajuan.lokasiUsaha) }

    // Nomor surat diatur agar awalnya kosong jika bernilai "-" atau blank
    var nomorSurat by remember {
        mutableStateOf(if (pengajuan.nomorSurat == "-") "" else pengajuan.nomorSurat)
    }

    // Pilihan Keputusan Kelurahan
    var isDiterima by remember { mutableStateOf(true) } // true = Diterima & Terbitkan, false = Ditolak
    var alasanDitolak by remember { mutableStateOf("") }

    // State Validasi & Dialog
    var showErrorValidation by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showImagePreviewDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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
                        text = "Koreksi & Edit Data Warga",
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
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // INFO BANNER KUNING
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFEF3C7),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Mode Koreksi & Edit Data Pemohon (${pengajuan.jenisSurat}):",
                        style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF92400E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Admin dapat langsung mengubah isian di bawah ini jika warga mengalami salah ketik (typo).",
                        style = AppTypography.BodyRegular,
                        color = Color(0xFF78350F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU FORMULIR EDIT DATA WARGA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "FORMULIR EDIT DATA WARGA",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    AdminEditTextField(
                        label = "Nama Lengkap",
                        value = nama,
                        onValueChange = { nama = it },
                        isError = showErrorValidation && nama.isBlank()
                    )
                    AdminEditTextField(
                        label = "NIK",
                        value = nik,
                        onValueChange = { nik = it },
                        isError = showErrorValidation && nik.isBlank()
                    )
                    AdminEditTextField(
                        label = "Tempat, Tanggal Lahir",
                        value = ttl,
                        onValueChange = { ttl = it },
                        isError = showErrorValidation && ttl.isBlank()
                    )
                    AdminEditTextField(
                        label = "Jenis Kelamin",
                        value = jenisKelamin,
                        onValueChange = { jenisKelamin = it },
                        isError = showErrorValidation && jenisKelamin.isBlank()
                    )
                    AdminEditTextField(
                        label = "Agama",
                        value = agama,
                        onValueChange = { agama = it },
                        isError = showErrorValidation && agama.isBlank()
                    )
                    AdminEditTextField(
                        label = "Status Perkawinan",
                        value = statusPerkawinan,
                        onValueChange = { statusPerkawinan = it },
                        isError = showErrorValidation && statusPerkawinan.isBlank()
                    )
                    AdminEditTextField(
                        label = "Pekerjaan",
                        value = pekerjaan,
                        onValueChange = { pekerjaan = it },
                        isError = showErrorValidation && pekerjaan.isBlank()
                    )

                    AdminEditTextField(
                        label = "Alamat Lengkap",
                        value = alamatLengkap,
                        onValueChange = { alamatLengkap = it },
                        singleLine = false,
                        minLines = 3,
                        maxLines = 4,
                        isError = showErrorValidation && alamatLengkap.isBlank()
                    )

                    // Jika surat berupa Surat Keterangan Usaha, tampilkan field detail usaha
                    if (pengajuan.jenisSurat.contains("Usaha", ignoreCase = true)) {
                        AdminEditTextField(
                            label = "Nama / Deskripsi Usaha",
                            value = namaUsaha,
                            onValueChange = { namaUsaha = it },
                            isError = showErrorValidation && namaUsaha.isBlank()
                        )
                        AdminEditTextField(
                            label = "Lokasi Usaha",
                            value = lokasiUsaha,
                            onValueChange = { lokasiUsaha = it },
                            singleLine = false,
                            minLines = 2,
                            maxLines = 3,
                            isError = showErrorValidation && lokasiUsaha.isBlank()
                        )
                    }

                    // Nomor surat resmi kini wajib diisi jika status diterima/terbitkan
                    AdminEditTextField(
                        label = "Nomor Surat Resmi (Diterbitkan Admin)",
                        value = nomorSurat,
                        onValueChange = { nomorSurat = it },
                        placeholder = "Masukkan nomor surat resmi",
                        isError = showErrorValidation && isDiterima && nomorSurat.isBlank()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Surat Pengantar RT (Lampiran Asli Warga)",
                        style = AppTypography.Caption,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GrayBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GrayBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = Icons.Default.AttachFile,
                                    contentDescription = null,
                                    tint = GreenDesaPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (pengajuan.pengantarUri.isNotBlank()) "Berkas Pengantar RT Terlampir" else "Tidak ada lampiran",
                                    style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.Medium),
                                    color = TextPrimary
                                )
                            }

                            if (pengajuan.pengantarUri.isNotBlank()) {
                                TextButton(onClick = { showImagePreviewDialog = true }) {
                                    Text(
                                        text = "Lihat Foto",
                                        style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                                        color = GreenDesaPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU PILIHAN KEPUTUSAN KELURAHAN
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "PILIHAN KEPUTUSAN KELURAHAN",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Opsi Diterima & Terbitkan
                    Surface(
                        onClick = { isDiterima = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDiterima) GreenDesaLight else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDiterima) GreenDesaPrimary else GrayBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isDiterima,
                                onClick = { isDiterima = true },
                                colors = RadioButtonDefaults.colors(selectedColor = GreenDesaPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Status: Diterima & Terbitkan Surat",
                                style = AppTypography.BodyRegular.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDiterima) GreenDesaPrimary else TextPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Opsi Ditolak Pengajuan
                    Surface(
                        onClick = { isDiterima = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (!isDiterima) RedRejectedContainer else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (!isDiterima) RedRejectedText else GrayBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !isDiterima,
                                onClick = { isDiterima = false },
                                colors = RadioButtonDefaults.colors(selectedColor = RedRejectedText)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Status: Ditolak Pengajuan",
                                style = AppTypography.BodyRegular.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (!isDiterima) RedRejectedText else TextPrimary
                                )
                            )
                        }
                    }

                    if (!isDiterima) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = "Alasan Penolakan (Wajib diisi jika ditolak):",
                                style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                                color = RedRejectedText
                            )
                            if (showErrorValidation && alasanDitolak.isBlank()) {
                                Text(text = "Wajib diisi", style = AppTypography.Caption.copy(color = Color(0xFFDC2626)))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = alasanDitolak,
                            onValueChange = { alasanDitolak = it },
                            placeholder = {
                                Text(
                                    "Contoh: Foto surat pengantar RT buram / tidak terbaca.",
                                    style = AppTypography.BodyRegular,
                                    color = TextPlaceholder
                                )
                            },
                            textStyle = AppTypography.BodyRegular.copy(fontWeight = FontWeight.SemiBold, color = RedRejectedText),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2,
                            maxLines = 4,
                            isError = showErrorValidation && alasanDitolak.isBlank(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = RedRejectedContainer.copy(alpha = 0.5f),
                                unfocusedContainerColor = RedRejectedContainer.copy(alpha = 0.3f),
                                focusedBorderColor = RedRejectedText,
                                unfocusedBorderColor = RedRejectedText.copy(alpha = 0.5f),
                                cursorColor = RedRejectedText,
                                errorBorderColor = Color(0xFFDC2626)
                            )
                        )
                    }
                }
            }

            // PESAN PERINGATAN GLOBAL JIKA ADA FIELD KOSONG
            val isFormIncomplete = nama.isBlank() || nik.isBlank() || ttl.isBlank() || jenisKelamin.isBlank() ||
                    agama.isBlank() || statusPerkawinan.isBlank() || pekerjaan.isBlank() || alamatLengkap.isBlank() ||
                    (isDiterima && nomorSurat.isBlank()) || (!isDiterima && alasanDitolak.isBlank())

            if (showErrorValidation && isFormIncomplete) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFEF2F2)
                ) {
                    Text(
                        text = "⚠️ Mohon lengkapi seluruh isian formulir koreksi serta nomor surat/alasan penolakan yang bertanda merah di atas.",
                        style = AppTypography.BodyRegular.copy(color = Color(0xFFDC2626), fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TOMBOL AKSI SIMPAN
            Button(
                onClick = {
                    val isFormValid = nama.isNotBlank() &&
                            nik.isNotBlank() &&
                            ttl.isNotBlank() &&
                            jenisKelamin.isNotBlank() &&
                            agama.isNotBlank() &&
                            statusPerkawinan.isNotBlank() &&
                            pekerjaan.isNotBlank() &&
                            alamatLengkap.isNotBlank() &&
                            (!isDiterima || nomorSurat.isNotBlank()) &&
                            (isDiterima || alasanDitolak.isNotBlank())

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
                Text(
                    text = "💾 Simpan Keputusan & Proses",
                    style = AppTypography.ButtonText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GrayBorder)
            ) {
                Text(
                    text = "Kembali ke Daftar",
                    style = AppTypography.ButtonText.copy(color = TextSecondary)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // DIALOG PRATINJAU FOTO FULL SCREEN DENGAN FITUR ZOOM & PAN
    if (showImagePreviewDialog && pengajuan.pengantarUri.isNotBlank()) {
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }

        AlertDialog(
            onDismissRequest = { showImagePreviewDialog = false },
            containerColor = Color.Black,
            shape = RoundedCornerShape(0.dp),
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            title = {},
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                offset = if (scale == 1f) androidx.compose.ui.geometry.Offset.Zero else offset + pan
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    coil.compose.AsyncImage(
                        model = Uri.parse(pengajuan.pengantarUri),
                        contentDescription = "Pratinjau Full Screen Surat Pengantar RT",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            ),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showImagePreviewDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(text = "Tutup Pratinjau", style = AppTypography.ButtonText)
                    }
                }
            },
            dismissButton = {}
        )
    }

    // DIALOG KONFIRMASI SIMPAN
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        color = if (isDiterima) GreenDesaLight else RedRejectedContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isDiterima) "✓" else "✕",
                                style = AppTypography.HeadingLarge.copy(
                                    color = if (isDiterima) GreenDesaPrimary else RedRejectedText
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isDiterima) "Yakin Terbitkan Surat?" else "Yakin Tolak Pengajuan Ini?",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Text(
                    text = if (isDiterima)
                        "Pastikan data koreksi warga dan nomor surat resmi sudah benar."
                    else
                        "Alasan penolakan akan dikirimkan ke warga.",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        val updatedItem = pengajuan.copy(
                            namaPemohon = nama,
                            nik = nik,
                            tempatTanggalLahir = ttl,
                            jenisKelamin = jenisKelamin,
                            agama = agama,
                            statusPerkawinan = statusPerkawinan,
                            pekerjaan = pekerjaan,
                            alamatLengkap = alamatLengkap,
                            namaUsaha = namaUsaha,
                            lokasiUsaha = lokasiUsaha,
                            nomorSurat = nomorSurat.ifBlank { "-" },
                            status = if (isDiterima) StatusPengajuan.DITERIMA else StatusPengajuan.DITOLAK,
                            pesanStatus = if (isDiterima) "Surat telah diterbitkan oleh kelurahan." else alasanDitolak
                        )
                        onSaveSuccess(updatedItem)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDiterima) GreenDesaPrimary else Color(0xFFDC2626)
                    )
                ) {
                    Text(
                        text = if (isDiterima) "Ya, Terbitkan Sekarang" else "Ya, Kirim Penolakan",
                        style = AppTypography.ButtonText
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDialog = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GrayBorder)
                ) {
                    Text(
                        text = "Batal",
                        style = AppTypography.ButtonText.copy(color = TextPrimary)
                    )
                }
            }
        )
    }
}

// Komponen Pendukung TextField Khusus Admin dengan Indikator Error Wajib Diisi
@Composable
fun AdminEditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false
) {
    val adminTextStyle = AppTypography.BodyRegular.copy(
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    )

    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = label,
                style = AppTypography.Caption,
                color = if (isError) Color(0xFFDC2626) else TextSecondary
            )
            if (isError) {
                Text(
                    text = "Wajib diisi",
                    style = AppTypography.Caption.copy(color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = adminTextStyle.copy(color = TextPlaceholder)
                )
            },
            textStyle = adminTextStyle,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = if (isError) Color(0xFFDC2626) else GrayBorder,
                focusedBorderColor = if (isError) Color(0xFFDC2626) else GreenDesaPrimary,
                cursorColor = GreenDesaPrimary,
                errorBorderColor = Color(0xFFDC2626)
            )
        )
    }
}