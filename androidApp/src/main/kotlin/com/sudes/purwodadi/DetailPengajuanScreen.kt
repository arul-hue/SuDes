package com.sudes.purwodadi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.sudes.purwodadi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengajuanScreen(
    pengajuanId: String = "",
    onBackClicked: () -> Unit = {}
) {
    var pengajuanItem by remember { mutableStateOf<PengajuanSurat?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(pengajuanId) {
        if (pengajuanId.isNotBlank()) {
            FirebaseFirestore.getInstance().collection("pengajuan_surat").document(pengajuanId)
                .get()
                .addOnSuccessListener { doc ->
                    isLoading = false
                    if (doc.exists()) {
                        @Suppress("UNCHECKED_CAST")
                        val tambahan = (doc.get("dataTambahan") as? Map<String, String>) ?: emptyMap()
                        pengajuanItem = PengajuanSurat(
                            id = doc.getString("id") ?: doc.id,
                            uid = doc.getString("uid") ?: "",
                            jenisSurat = doc.getString("jenisSurat") ?: "",
                            nomorSurat = doc.getString("nomorSurat") ?: "-",
                            tanggalPengajuan = doc.getString("tanggalPengajuan") ?: "",
                            status = doc.getString("status") ?: "Proses",
                            alasanPenolakan = doc.getString("alasanPenolakan") ?: "",
                            namaLengkap = doc.getString("namaLengkap") ?: "",
                            nik = doc.getString("nik") ?: "",
                            tempatTanggalLahir = doc.getString("tempatTanggalLahir") ?: "",
                            jenisKelamin = doc.getString("jenisKelamin") ?: "",
                            agama = doc.getString("agama") ?: "",
                            statusPerkawinan = doc.getString("statusPerkawinan") ?: "",
                            pekerjaan = doc.getString("pekerjaan") ?: "",
                            alamatLengkap = doc.getString("alamatLengkap") ?: "",
                            keperluanSurat = doc.getString("keperluanSurat") ?: "",
                            lampiranPengantarRt = doc.getString("lampiranPengantarRt") ?: "",
                            dataTambahan = tambahan
                        )
                    }
                }
                .addOnFailureListener {
                    isLoading = false
                }
        } else {
            isLoading = false
        }
    }

    val scrollState = rememberScrollState()

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
                        text = "Detail Pengajuan Surat",
                        style = AppTypography.HeadingMedium.copy(color = Color.White)
                    )
                }
            }
        },
        containerColor = GrayBackground
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GreenDesaPrimary)
            }
        } else if (pengajuanItem == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Data pengajuan tidak ditemukan.", style = AppTypography.BodyRegular, color = TextSecondary)
            }
        } else {
            val item = pengajuanItem!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusFirestoreBadge(status = item.status)
                            Text(text = item.tanggalPengajuan, style = AppTypography.Caption, color = TextSecondary)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = item.jenisSurat, style = AppTypography.HeadingMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Nomor Surat Resmi: ${item.nomorSurat}", style = AppTypography.BodyRegular, color = GreenDesaPrimary, fontWeight = FontWeight.Bold)

                        if (item.status.equals("Ditolak", ignoreCase = true) && item.alasanPenolakan.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = RedRejectedContainer
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = "Alasan Penolakan:", style = AppTypography.Caption, color = RedRejectedText, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = item.alasanPenolakan, style = AppTypography.BodyRegular, color = RedRejectedText)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "INFORMASI PEMOHON", style = AppTypography.HeadingMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderColor, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        DetailFieldRow(label = "Nama Lengkap", value = item.namaLengkap)
                        DetailFieldRow(label = "NIK", value = item.nik)
                        DetailFieldRow(label = "Tempat, Tanggal Lahir", value = item.tempatTanggalLahir)
                        DetailFieldRow(label = "Jenis Kelamin", value = item.jenisKelamin)
                        DetailFieldRow(label = "Agama", value = item.agama)
                        DetailFieldRow(label = "Status Perkawinan", value = item.statusPerkawinan)
                        DetailFieldRow(label = "Pekerjaan", value = item.pekerjaan)
                        DetailFieldRow(label = "Alamat Lengkap", value = item.alamatLengkap)
                        DetailFieldRow(label = "Keperluan Surat", value = item.keperluanSurat)

                        item.dataTambahan.forEach { (key, value) ->
                            DetailFieldRow(label = key.replaceFirstChar { it.uppercase() }, value = value)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "LAMPIRAN DOKUMEN", style = AppTypography.HeadingMedium, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderColor, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "File Pengantar RT: ${item.lampiranPengantarRt}", style = AppTypography.BodyRegular, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun DetailFieldRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(text = label, style = AppTypography.Caption, color = TextSecondary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value.ifEmpty { "-" }, style = AppTypography.BodyRegular, color = TextPrimary)
    }
}