package com.sudes.purwodadi

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
    var pekerjaan by remember { mutableStateOf(pengajuan.pekerjaan) }
    var alamatLengkap by remember { mutableStateOf(pengajuan.alamatLengkap) }

    // Field khusus Detail Usaha (jika jenis surat adalah Surat Keterangan Usaha)
    var namaUsaha by remember { mutableStateOf(pengajuan.namaUsaha) }
    var lokasiUsaha by remember { mutableStateOf(pengajuan.lokasiUsaha) }

    var nomorSurat by remember { mutableStateOf(pengajuan.nomorSurat) }

    // Pilihan Keputusan Kelurahan
    var isDiterima by remember { mutableStateOf(true) } // true = Diterima & Terbitkan, false = Ditolak
    var alasanDitolak by remember { mutableStateOf("") }

    // State Dialog Konfirmasi & Preview Foto Lampiran
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

            // KARTU FORMULIR EDIT DATA WARGA (BERISI DATA ASLI DARI INPUTAN USER)
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

                    AdminEditTextField(label = "Nama Lengkap", value = nama, onValueChange = { nama = it })
                    AdminEditTextField(label = "NIK", value = nik, onValueChange = { nik = it })
                    AdminEditTextField(label = "Tempat, Tanggal Lahir", value = ttl, onValueChange = { ttl = it })
                    AdminEditTextField(label = "Jenis Kelamin", value = jenisKelamin, onValueChange = { jenisKelamin = it })
                    AdminEditTextField(label = "Agama", value = agama, onValueChange = { agama = it })
                    AdminEditTextField(label = "Pekerjaan", value = pekerjaan, onValueChange = { pekerjaan = it })

                    AdminEditTextField(
                        label = "Alamat Lengkap",
                        value = alamatLengkap,
                        onValueChange = { alamatLengkap = it },
                        singleLine = false,
                        minLines = 3,
                        maxLines = 4
                    )

                    // Jika surat berupa Surat Keterangan Usaha, tampilkan field detail usaha milik user
                    if (pengajuan.jenisSurat.contains("Usaha", ignoreCase = true)) {
                        AdminEditTextField(label = "Nama / Deskripsi Usaha", value = namaUsaha, onValueChange = { namaUsaha = it })
                        AdminEditTextField(
                            label = "Lokasi Usaha",
                            value = lokasiUsaha,
                            onValueChange = { lokasiUsaha = it },
                            singleLine = false,
                            minLines = 2,
                            maxLines = 3
                        )
                    }

                    AdminEditTextField(label = "Nomor Surat Resmi (Diterbitkan Admin)", value = nomorSurat, onValueChange = { nomorSurat = it })

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
                        Text(
                            text = "Alasan Penolakan (Wajib diisi jika ditolak):",
                            style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                            color = RedRejectedText
                        )
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
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = RedRejectedContainer.copy(alpha = 0.5f),
                                unfocusedContainerColor = RedRejectedContainer.copy(alpha = 0.3f),
                                focusedBorderColor = RedRejectedText,
                                unfocusedBorderColor = RedRejectedText.copy(alpha = 0.5f),
                                cursorColor = RedRejectedText
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TOMBOL AKSI SIMPAN
            Button(
                onClick = { showConfirmDialog = true },
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

    // DIALOG PRATINJAU FOTO ASLI PENGANTAR RT DARI WARGA
    if (showImagePreviewDialog && pengajuan.pengantarUri.isNotBlank()) {
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
                                model = Uri.parse(pengajuan.pengantarUri),
                                contentDescription = "Pratinjau Surat Pengantar RT Warga",
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
                        // Buat salinan objek yang diperbarui oleh admin
                        val updatedItem = pengajuan.copy(
                            namaPemohon = nama,
                            nik = nik,
                            tempatTanggalLahir = ttl,
                            jenisKelamin = jenisKelamin,
                            agama = agama,
                            pekerjaan = pekerjaan,
                            alamatLengkap = alamatLengkap,
                            namaUsaha = namaUsaha,
                            lokasiUsaha = lokasiUsaha,
                            nomorSurat = nomorSurat,
                            status = if (isDiterima) StatusPengajuan.DITERIMA else StatusPengajuan.DITOLAK,
                            pesanStatus = if (isDiterima) "Surat telah diterbitkan oleh kelurahan." else alasanDitolak.ifBlank { "Pengajuan ditolak oleh admin." }
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

// Komponen Pendukung TextField Khusus Admin
@Composable
fun AdminEditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    val adminTextStyle = AppTypography.BodyRegular.copy(
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    )

    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = AppTypography.Caption,
            color = TextSecondary
        )
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = GrayBorder,
                focusedBorderColor = GreenDesaPrimary,
                cursorColor = GreenDesaPrimary
            )
        )
    }
}