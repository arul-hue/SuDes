package com.sudes.purwodadi

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
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@Composable
fun AdminEditPengajuanScreen(
    jenisSurat: String = "Surat Keterangan Domisili",
    namaPemohonAwal: String = "Sahal Alfarizi",
    nikAwal: String = "3501230101900001",
    ttlAwal: String = "Bandar Lampung, 01 Januari 1990",
    jenisKelaminAwal: String = "Laki-laki",
    agamaAwal: String = "Islam",
    pekerjaanAwal: String = "Wiraswasta",
    alamatLengkapAwal: String = "Jl. Melati No. 12, RT 03/RW 05, Desa Purwodadi Simpang, Kec. Tanjung Bintang",
    alamatTinggalAwal: String = "Dusun II RT 03, Purwodadi Simpang, Kec. Tanjung Bintang, Lampung Selatan",
    nomorSuratAwal: String = "470 / 045 / SKD / XI / 2026",
    namaFileRt: String = "pengantar_rt03_sahal.jpg",
    onBackClicked: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    var nama by remember { mutableStateOf(namaPemohonAwal) }
    var nik by remember { mutableStateOf(nikAwal) }
    var ttl by remember { mutableStateOf(ttlAwal) }
    var jenisKelamin by remember { mutableStateOf(jenisKelaminAwal) }
    var agama by remember { mutableStateOf(agamaAwal) }
    var pekerjaan by remember { mutableStateOf(pekerjaanAwal) }
    var alamatLengkap by remember { mutableStateOf(alamatLengkapAwal) }
    var alamatTinggal by remember { mutableStateOf(alamatTinggalAwal) }
    var nomorSurat by remember { mutableStateOf(nomorSuratAwal) }

    // Pilihan Keputusan Kelurahan
    var isDiterima by remember { mutableStateOf(true) } // true = Diterima & Terbitkan, false = Ditolak
    var alasanDitolak by remember { mutableStateOf("") }

    // Dialog Konfirmasi
    var showConfirmDialog by remember { mutableStateOf(false) }

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
                        text = "Mode Koreksi & Edit Data Pemohon:",
                        style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF92400E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Admin dapat langsung mengubah isian jika warga mengalami salah ketik (typo) pada data di bawah.",
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

                    AdminEditTextField(label = "Nama Lengkap", value = nama, onValueChange = { nama = it })
                    AdminEditTextField(label = "NIK", value = nik, onValueChange = { nik = it })
                    AdminEditTextField(label = "Tempat, Tanggal Lahir", value = ttl, onValueChange = { ttl = it })
                    AdminEditTextField(label = "Jenis Kelamin", value = jenisKelamin, onValueChange = { jenisKelamin = it })
                    AdminEditTextField(label = "Agama", value = agama, onValueChange = { agama = it })
                    AdminEditTextField(label = "Pekerjaan", value = pekerjaan, onValueChange = { pekerjaan = it })
                    AdminEditTextField(label = "Alamat Lengkap", value = alamatLengkap, onValueChange = { alamatLengkap = it })
                    AdminEditTextField(label = "Alamat Tempat Tinggal Saat Ini", value = alamatTinggal, onValueChange = { alamatTinggal = it })

                    AdminEditTextField(label = "Nomor Surat Resmi (Diterbitkan Admin)", value = nomorSurat, onValueChange = { nomorSurat = it })

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Surat Pengantar RT (Lampiran Warga)",
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AttachFile,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = namaFileRt,
                                    style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.Medium),
                                    color = TextPrimary
                                )
                            }
                            TextButton(onClick = { }) {
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
                                    "Contoh: Foto surat pengantar RT buram dan tidak terbaca dengan jelas. Mohon ajukan ulang.",
                                    style = AppTypography.BodyRegular,
                                    color = TextPlaceholder
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
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

            // TOMBOL AKSI
            OutlinedButton(
                onClick = { /* Pratinjau */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = GreenDesaLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenDesaPrimary)
            ) {
                Text(
                    text = "👁 Pratinjau Hasil Surat (Preview)",
                    style = AppTypography.ButtonText.copy(color = GreenDesaPrimary)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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

    // DIALOG KONFIRMASI
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
                        "Pastikan data warga dan nomor surat resmi sudah benar dan bebas typo."
                    else
                        "Alasan penolakan akan dikirimkan ke warga agar mereka dapat melakukan ajukan ulang.",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onSaveSuccess()
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
                        text = if (isDiterima) "Periksa Kembali" else "Batal",
                        style = AppTypography.ButtonText.copy(color = TextPrimary)
                    )
                }
            }
        )
    }
}

@Composable
fun AdminEditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = ""
) {
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
            placeholder = { Text(placeholder, style = AppTypography.BodyRegular, color = TextPlaceholder) },
            textStyle = AppTypography.BodyRegular.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
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