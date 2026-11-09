package com.sudes.purwodadi

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getSktmDateFormatted(): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
    return formatter.format(Date())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuratTidakMampuScreen(
    onBackClicked: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val suratViewModel = remember { SuratViewModel() }
    val isLoading by suratViewModel.isLoading.collectAsState()

    var namaLengkap by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var tempatTanggalLahir by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("") }
    var agama by remember { mutableStateOf("") }
    var statusPerkawinan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var alamatLengkap by remember { mutableStateOf("") }

    var nomorKK by remember { mutableStateOf("") }
    var namaKepalaKeluarga by remember { mutableStateOf("") }
    var keperluanSurat by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    var showConfirmDialog by remember { mutableStateOf(false) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    BackHandler {
        when {
            isSubmittedSuccess -> {
                isSubmittedSuccess = false
                onSubmitSuccess()
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
                        text = "Surat Keterangan Tidak Mampu",
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
            Surface(
                onClick = {
                    suratViewModel.ambilDataProfilWarga(
                        onResult = { data ->
                            namaLengkap = (data["namaLengkap"] as? String)?.takeIf { it != "-" } ?: ""
                            nik = (data["nik"] as? String)?.takeIf { it != "-" } ?: ""
                            tempatTanggalLahir = (data["tempatTanggalLahir"] as? String)?.takeIf { it != "-" } ?: ""
                            agama = (data["agama"] as? String)?.takeIf { it != "-" } ?: ""
                            statusPerkawinan = (data["statusPerkawinan"] as? String)?.takeIf { it != "-" } ?: ""
                            pekerjaan = (data["pekerjaan"] as? String)?.takeIf { it != "-" } ?: ""
                            alamatLengkap = (data["alamatLengkap"] as? String)?.takeIf { it != "-" } ?: ""
                            Toast.makeText(context, "Data profil berhasil diisi otomatis!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { pesanError ->
                            Toast.makeText(context, pesanError, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
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
                    Text(text = "⚡  ", style = AppTypography.HeadingMedium)
                    Text(
                        text = if (isLoading) "Memuat Data Profil..." else "Isi Otomatis dari Profil",
                        style = AppTypography.LabelForm.copy(color = GreenDesaPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SktmCardSection(title = "DATA DIRI") {
                SktmFormTextField(label = "Nama Lengkap", value = namaLengkap, onValueChange = { namaLengkap = it }, placeholder = "Masukkan nama lengkap")
                SktmFormTextField(label = "NIK", value = nik, onValueChange = { nik = it }, placeholder = "Masukkan 16 digit NIK")
                SktmFormTextField(label = "Tempat, Tanggal Lahir", value = tempatTanggalLahir, onValueChange = { tempatTanggalLahir = it }, placeholder = "Contoh: Bandar Lampung, 01 Januari 1990")

                SktmFormDropdownField(
                    label = "Jenis Kelamin",
                    selectedValue = jenisKelamin,
                    placeholder = "Pilih jenis kelamin",
                    options = listOf("Laki-laki", "Perempuan"),
                    onOptionSelected = { jenisKelamin = it }
                )
                SktmFormDropdownField(
                    label = "Agama",
                    selectedValue = agama,
                    placeholder = "Pilih agama",
                    options = listOf("Islam", "Kristen", "Katolik", "Hindu", "Buddha", "Konghucu"),
                    onOptionSelected = { agama = it }
                )
                SktmFormDropdownField(
                    label = "Status Perkawinan",
                    selectedValue = statusPerkawinan,
                    placeholder = "Pilih status perkawinan",
                    options = listOf("Belum Kawin", "Kawin", "Cerai Hidup", "Cerai Mati"),
                    onOptionSelected = { statusPerkawinan = it }
                )

                SktmFormTextField(label = "Pekerjaan", value = pekerjaan, onValueChange = { pekerjaan = it }, placeholder = "Masukkan pekerjaan")

                SktmFormTextField(
                    label = "Alamat Lengkap",
                    value = alamatLengkap,
                    onValueChange = { alamatLengkap = it },
                    placeholder = "Masukkan alamat sesuai KTP",
                    singleLine = false,
                    minLines = 3,
                    maxLines = 4
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SktmCardSection(title = "DETAIL KELUARGA & KEPERLUAN") {
                SktmFormTextField(label = "Nomor Kartu Keluarga (KK)", value = nomorKK, onValueChange = { nomorKK = it }, placeholder = "Masukkan nomor KK")
                SktmFormTextField(label = "Nama Kepala Keluarga (di KK)", value = namaKepalaKeluarga, onValueChange = { namaKepalaKeluarga = it }, placeholder = "Contoh: Budi Santoso")

                SktmFormTextField(
                    label = "Keperluan Pembuatan Surat",
                    value = keperluanSurat,
                    onValueChange = { keperluanSurat = it },
                    placeholder = "Contoh: Persyaratan Berobat / Sekolah / Beasiswa",
                    singleLine = false,
                    minLines = 2,
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SktmCardSection(title = "SURAT PENGANTAR RT") {
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedImageUri != null) GreenDesaLight else GrayBackground
                    )
                ) {
                    Icon(
                        imageVector = if (selectedImageUri != null) Icons.Default.Check else Icons.Default.AttachFile,
                        contentDescription = "Upload",
                        tint = if (selectedImageUri != null) GreenDesaPrimary else TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedImageUri != null) "Foto Pengantar RT Siap Diunggah" else "Unggah Foto/Scan Surat Pengantar RT",
                        color = if (selectedImageUri != null) GreenDesaPrimary else TextSecondary,
                        style = AppTypography.BodyRegular
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SktmCardSection(title = "INFORMASI SURAT") {
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
                        Text(text = "Tanggal", style = AppTypography.Caption, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = getSktmDateFormatted(),
                            style = AppTypography.LabelForm,
                            color = GreenDesaPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showConfirmDialog = true },
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
                            suratViewModel.kirimPengajuanSurat(
                                jenisSurat = "Surat Keterangan Tidak Mampu",
                                tanggalPengajuan = getSktmDateFormatted(),
                                namaLengkap = namaLengkap,
                                nik = nik,
                                tempatTanggalLahir = tempatTanggalLahir,
                                jenisKelamin = jenisKelamin,
                                agama = agama,
                                statusPerkawinan = statusPerkawinan,
                                pekerjaan = pekerjaan,
                                alamatLengkap = alamatLengkap,
                                keperluanSurat = keperluanSurat,
                                lampiranPengantarRt = selectedImageUri?.toString() ?: "pengantar_rt.jpg",
                                dataTambahan = mapOf(
                                    "nomorKK" to nomorKK,
                                    "namaKepalaKeluarga" to namaKepalaKeluarga
                                ),
                                onSuccess = {
                                    showConfirmDialog = false
                                    isSubmittedSuccess = true
                                },
                                onError = { pesanError ->
                                    showConfirmDialog = false
                                    Toast.makeText(context, pesanError, Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(
                            text = if (isLoading) "Mengirim..." else "Ya, Kirim Sekarang",
                            style = AppTypography.ButtonText
                        )
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
                Text(text = "Dokumen Anda telah masuk ke sistem desa dan menunggu verifikasi.", style = AppTypography.BodyRegular, color = TextSecondary, textAlign = TextAlign.Center)
            },
            confirmButton = {
                Button(
                    onClick = {
                        isSubmittedSuccess = false
                        onSubmitSuccess()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(text = "Kembali ke Beranda", style = AppTypography.ButtonText)
                }
            }
        )
    }
}

@Composable
fun SktmCardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
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

@Composable
fun SktmFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
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

@Composable
fun SktmFormDropdownField(
    label: String,
    selectedValue: String,
    placeholder: String = "",
    options: List<String> = emptyList(),
    onOptionSelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = label, style = AppTypography.LabelForm, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { if (options.isNotEmpty()) expanded = true },
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
                        text = selectedValue.ifEmpty { placeholder },
                        color = if (selectedValue.isEmpty()) TextPlaceholder else TextPrimary,
                        style = AppTypography.BodyRegular
                    )
                    Text(text = "▼", color = TextSecondary, style = AppTypography.Caption)
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option, style = AppTypography.BodyRegular, color = TextPrimary) },
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