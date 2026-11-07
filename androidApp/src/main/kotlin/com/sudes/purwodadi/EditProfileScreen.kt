package com.sudes.purwodadi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    initialNama: String = "Sahal Alfarizi",
    onBackClicked: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    // State Form Edit Data Diri
    var namaLengkap by remember { mutableStateOf(initialNama) }
    var nik by remember { mutableStateOf("") }
    var tempatTanggalLahir by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var agama by remember { mutableStateOf("") }
    var statusPerkawinan by remember { mutableStateOf("") }
    var alamatLengkap by remember { mutableStateOf("") }

    // State untuk Mengatur Munculnya Pop-up
    var showConfirmDialog by remember { mutableStateOf(false) } // Pop-up Yakin / Konfirmasi
    var isSavedSuccess by remember { mutableStateOf(false) }       // Pop-up Sukses Berhasil Disimpan

    val scrollState = rememberScrollState()

    // TAMPILAN FORM UTAMA EDIT DATA DIRI
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
                        text = "Edit Data Diri",
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
            EditProfileTextField(
                label = "Nama Lengkap",
                value = namaLengkap,
                onValueChange = { namaLengkap = it },
                placeholder = "Masukkan nama lengkap"
            )
            EditProfileTextField(
                label = "NIK (Nomor Induk Kependudukan)",
                value = nik,
                onValueChange = { nik = it },
                placeholder = "Masukkan 16 digit NIK"
            )
            EditProfileTextField(
                label = "Tempat, Tanggal Lahir",
                value = tempatTanggalLahir,
                onValueChange = { tempatTanggalLahir = it },
                placeholder = "Contoh: Bandar Lampung, 01 Januari 1990"
            )
            EditProfileTextField(
                label = "Pekerjaan",
                value = pekerjaan,
                onValueChange = { pekerjaan = it },
                placeholder = "Masukkan jenis pekerjaan"
            )
            EditProfileDropdownField(
                label = "Agama",
                selectedValue = agama.ifEmpty { "Pilih agama" }
            )
            EditProfileDropdownField(
                label = "Status Perkawinan",
                selectedValue = statusPerkawinan.ifEmpty { "Pilih status perkawinan" }
            )
            EditProfileTextField(
                label = "Alamat Lengkap",
                value = alamatLengkap,
                onValueChange = { alamatLengkap = it },
                placeholder = "Masukkan alamat lengkap sesuai KTP"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // TOMBOL SIMPAN PERUBAHAN (Memicu Pop-up Konfirmasi Terlebih Dahulu)
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
            ) {
                Text(
                    text = "Simpan Perubahan",
                    style = AppTypography.ButtonText
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // 1. POP-UP DIALOG KONFIRMASI ("Apakah Anda yakin data sudah benar?")
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
                        shape = CircleShape,
                        color = Color(0xFFFEF3C7) // Kuning lembut peringatan
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "?",
                                style = AppTypography.HeadingLarge,
                                color = Color(0xFFD97706)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Simpan Perubahan Data?",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    text = "Pastikan data diri yang Anda masukkan sudah benar",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            showConfirmDialog = false
                            isSavedSuccess = true // Lanjut memunculkan pop-up sukses
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(text = "Ya, Simpan", style = AppTypography.ButtonText)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { showConfirmDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Periksa Kembali", color = TextPrimary, style = AppTypography.ButtonText)
                    }
                }
            },
            dismissButton = {}
        )
    }

    // 2. POP-UP DIALOG SUKSES BERHASIL DISIMPAN
    if (isSavedSuccess) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        color = GreenDesaLight
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Berhasil Disimpan!",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Text(
                    text = "Data diri Anda telah berhasil diperbarui.",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        isSavedSuccess = false
                        onSaveSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(
                        text = "Kembali ke Profil",
                        style = AppTypography.ButtonText
                    )
                }
            }
        )
    }
}

// Komponen Input TextField Khusus Edit Profil (Menggunakan Root Typo)
@Composable
fun EditProfileTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, style = AppTypography.LabelForm, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextPlaceholder, style = AppTypography.BodyRegular) },
            textStyle = AppTypography.BodyRegular.copy(color = TextPrimary),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                unfocusedBorderColor = GrayBorder,
                focusedBorderColor = GreenDesaPrimary,
                focusedLabelColor = GreenDesaPrimary,
                cursorColor = GreenDesaPrimary
            )
        )
    }
}

// Komponen Dropdown Simulasi Khusus Edit Profil
@Composable
fun EditProfileDropdownField(label: String, selectedValue: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, style = AppTypography.LabelForm, color = TextPrimary)
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
                Text(text = selectedValue, color = TextPlaceholder, style = AppTypography.BodyRegular)
                Text(text = "▼", color = TextSecondary, style = AppTypography.Caption)
            }
        }
    }
}