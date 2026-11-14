package com.sudes.purwodadi

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

// Data class pendukung untuk menampung pasangan Label dan Nilai secara dinamis
data class DetailFieldItem(
    val label: String,
    val value: String
)

@Composable
fun DetailPengajuanScreen(
    jenisSurat: String = "Surat Keterangan Usaha",
    status: StatusPengajuan = StatusPengajuan.PROSES,
    tanggalPengajuan: String = "03 Nov 2026, 09.15 WIB", // Waktu awal user mengajukan surat
    tanggalKeputusan: String = tanggalPengajuan,       // Waktu saat admin menolak/menerima
    alasanDitolak: String? = null,
    namaFileRt: String = "pengantar_rt.jpg",
    pengantarUriString: String = "", // 👈 Menangkap URI file asli yang diunggah warga
    fields: List<DetailFieldItem> = emptyList(),
    onBackClicked: () -> Unit = {},
    onResubmitClicked: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var showImagePreviewDialog by remember { mutableStateOf(false) }

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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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

                    Box(modifier = Modifier.padding(end = 12.dp)) {
                        StatusBadge(status = status)
                    }
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
            // KARTU 1: STATUS TRACKER (PROGRES SURAT DINAMIS)
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
                        text = jenisSurat,
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    when (status) {
                        StatusPengajuan.PROSES -> {
                            TimelineStepItem(
                                title = "1. Diproses — Berkas dicek perangkat desa",
                                date = tanggalPengajuan, // Waktu saat user mengajukan surat
                                isCompleted = true,
                                isProcess = true,
                                showLine = true
                            )
                            TimelineStepItem(
                                title = "2. Selesai — Surat dapat diambil di Balai Desa",
                                date = "Menunggu verifikasi selesai",
                                isCompleted = false,
                                isProcess = false,
                                showLine = false
                            )
                        }
                        StatusPengajuan.DITERIMA -> {
                            TimelineStepItem(
                                title = "1. Diproses — Berkas telah diverifikasi",
                                date = tanggalPengajuan, // Waktu saat user mengajukan surat
                                isCompleted = true,
                                isProcess = false,
                                showLine = true
                            )
                            TimelineStepItem(
                                title = "2. Selesai — Surat dapat diambil di Balai Desa",
                                date = tanggalKeputusan, // Waktu saat admin menerima/menyetujui
                                isCompleted = true,
                                isProcess = false,
                                showLine = false
                            )
                        }
                        StatusPengajuan.DITOLAK -> {
                            TimelineStepItem(
                                title = "1. Diproses — Berkas diterima perangkat desa",
                                date = tanggalPengajuan, // Waktu saat user mengajukan surat
                                isCompleted = true,
                                isProcess = false,
                                showLine = true
                            )
                            TimelineStepItem(
                                title = "2. Ditolak — Pengajuan perlu diperbaiki",
                                date = tanggalKeputusan, // Waktu saat admin menolak
                                isCompleted = true,
                                isProcess = false,
                                isRejected = true,
                                showLine = false
                            )

                            if (!alasanDitolak.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = RedRejectedContainer,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, RedRejectedBorder.copy(alpha = 0.3f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Alasan Penolakan Admin:",
                                            style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                                            color = RedRejectedText
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = alasanDitolak,
                                            style = AppTypography.BodyRegular,
                                            color = RedRejectedDarkText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // KARTU 2: DATA YANG DIINPUTKAN USER (TERMASUK STATUS PERKAWINAN & DETAIL USAHA)
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
                        text = "DATA YANG ANDA INPUTKAN",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = BorderColor, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (fields.isEmpty()) {
                        Text(
                            text = "Tidak ada data formulir.",
                            style = AppTypography.BodyRegular,
                            color = TextSecondary
                        )
                    } else {
                        fields.forEach { field ->
                            DetailItemRow(label = field.label, value = field.value)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Surat Pengantar RT",
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
                                    text = if (pengantarUriString.isNotBlank()) "pengantar_rt_warga.jpg" else "Tidak ada lampiran",
                                    style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.Medium),
                                    color = TextPrimary
                                )
                            }
                            if (pengantarUriString.isNotBlank()) {
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

            Spacer(modifier = Modifier.height(24.dp))

            if (status == StatusPengajuan.DITOLAK) {
                Button(
                    onClick = { onResubmitClicked(jenisSurat) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(
                        text = "Ajukan Ulang Surat Ini",
                        style = AppTypography.ButtonText
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = onBackClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (status == StatusPengajuan.DITOLAK) Color.Transparent else GreenDesaPrimary
                ),
                border = if (status == StatusPengajuan.DITOLAK) androidx.compose.foundation.BorderStroke(1.dp, GreenDesaPrimary) else null
            ) {
                Text(
                    text = "Kembali ke Status",
                    style = AppTypography.ButtonText.copy(
                        color = if (status == StatusPengajuan.DITOLAK) GreenDesaPrimary else Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // DIALOG PRATINJAU FOTO FULL SCREEN DENGAN FITUR ZOOM & PAN
    if (showImagePreviewDialog && pengantarUriString.isNotBlank()) {
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
                        model = Uri.parse(pengantarUriString),
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
}

@Composable
fun DetailItemRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = AppTypography.Caption,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.SemiBold),
            color = TextPrimary
        )
    }
}

@Composable
fun TimelineStepItem(
    title: String,
    date: String,
    isCompleted: Boolean,
    isProcess: Boolean,
    isRejected: Boolean = false,
    showLine: Boolean
) {
    val dotColor = when {
        isRejected -> RedRejectedText
        isProcess -> OrangeProcessText
        isCompleted -> GreenDesaPrimary
        else -> Color(0xFFCBD5E1)
    }

    val textColor = when {
        isRejected -> RedRejectedText
        isProcess -> OrangeProcessText
        isCompleted -> GreenDesaPrimary
        else -> TextSecondary
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            if (showLine) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(45.dp)
                        .background(GrayBorder)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = date,
                style = AppTypography.Caption,
                color = TextSecondary
            )
        }
    }
}