package com.sudes.purwodadi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    initialNama: String = "",
    initialNik: String = "",
    initialTempatTgl: String = "",
    initialPekerjaan: String = "",
    initialAgama: String = "",
    initialStatus: String = "",
    initialAlamat: String = "",
    onBackClicked: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    // State Form Edit Data Diri diisi dengan data awal dari database
    var namaLengkap by remember { mutableStateOf(initialNama) }
    var nik by remember { mutableStateOf(initialNik) }
    var tempatTanggalLahir by remember { mutableStateOf(initialTempatTgl) }
    var pekerjaan by remember { mutableStateOf(initialPekerjaan) }
    var agama by remember { mutableStateOf(initialAgama) }
    var statusPerkawinan by remember { mutableStateOf(initialStatus) }
    var alamatLengkap by remember { mutableStateOf(initialAlamat) }

    // State untuk Mengatur Munculnya Pop-up
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isSavedSuccess by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading.collectAsState()
    val scrollState = rememberScrollState()

    // BackHandler untuk mencegat tombol Back fisik/gestur HP
    BackHandler {
        if (isSavedSuccess) {
            isSavedSuccess = false
            onSaveSuccess()
        } else if (showConfirmDialog) {
            showConfirmDialog = false
        } else {
            onBackClicked()
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

            // DROPDOWN AGAMA INTERAKTIF
            EditProfileDropdownField(
                label = "Agama",
                selectedValue = agama,
                options = listOf("Islam", "Kristen Protestan", "Kristen Katolik", "Hindu", "Buddha", "Konghucu"),
                onOptionSelected = { agama = it }
            )

            // DROPDOWN STATUS PERKAWINAN INTERAKTIF
            EditProfileDropdownField(
                label = "Status Perkawinan",
                selectedValue = statusPerkawinan,
                options = listOf("Belum Kawin", "Kawin", "Cerai Hidup", "Cerai Mati"),
                onOptionSelected = { statusPerkawinan = it }
            )

            // ALAMAT LENGKAP MULTI-LINE
            EditProfileTextField(
                label = "Alamat Lengkap",
                value = alamatLengkap,
                onValueChange = { alamatLengkap = it },
                placeholder = "Masukkan alamat lengkap sesuai KTP",
                singleLine = false,
                minLines = 3,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            // TOMBOL SIMPAN PERUBAHAN
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Simpan Perubahan",
                        style = AppTypography.ButtonText
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // 1. POP-UP DIALOG KONFIRMASI
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
                        color = Color(0xFFFEF3C7)
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
                            // Panggil fungsi update ke Firestore melalui AuthViewModel
                            authViewModel.updateProfileData(
                                namaLengkap = namaLengkap,
                                nik = nik,
                                tempatTanggalLahir = tempatTanggalLahir,
                                pekerjaan = pekerjaan,
                                agama = agama,
                                statusPerkawinan = statusPerkawinan,
                                alamatLengkap = alamatLengkap,
                                onSuccess = {
                                    isSavedSuccess = true
                                },
                                onError = { _ -> }
                            )
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

    // 2. POP-UP DIALOG SUKSES
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

// Komponen Input TextField Khusus Edit Profil
@Composable
fun EditProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1
) {
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
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
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

// Komponen Dropdown Interaktif Khusus Edit Profil
@Composable
fun EditProfileDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, style = AppTypography.LabelForm, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = !expanded },
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
                    Text(
                        text = if (selectedValue.isBlank()) "Pilih $label" else selectedValue,
                        color = if (selectedValue.isBlank()) TextPlaceholder else TextPrimary,
                        style = AppTypography.BodyRegular
                    )
                    Text(text = if (expanded) "▲" else "▼", color = TextSecondary, style = AppTypography.Caption)
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, style = AppTypography.BodyRegular, color = TextPrimary) },
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