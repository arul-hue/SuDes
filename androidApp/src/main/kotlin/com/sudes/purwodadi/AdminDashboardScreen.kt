package com.sudes.purwodadi

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@Composable
fun AdminDashboardScreen(
    listPengajuan: List<PengajuanItem> = emptyList(), // 👈 Menerima data real-time dari MainActivity
    onDetailClicked: (PengajuanItem) -> Unit = {},    // 👈 Mengirimkan objek pengajuan lengkap
    onSelesaikanClicked: (PengajuanItem) -> Unit = {},// 👈 Mengirimkan objek pengajuan lengkap
    onLogoutClicked: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    // State untuk mengontrol pop-up konfirmasi logout
    var showLogoutDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val filterScrollState = rememberScrollState()

    // Logika Filter & Pencarian Admin berdasarkan data real-time
    val filteredList = listPengajuan.filter { item ->
        val matchSearch = item.jenisSurat.contains(searchQuery, ignoreCase = true) ||
                item.namaPemohon.contains(searchQuery, ignoreCase = true)
        val matchFilter = when (selectedFilter) {
            "Proses" -> item.status == StatusPengajuan.PROSES
            "Diterima" -> item.status == StatusPengajuan.DITERIMA
            "Ditolak" -> item.status == StatusPengajuan.DITOLAK
            else -> true
        }
        matchSearch && matchFilter
    }

    Scaffold(
        containerColor = GrayBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // HEADER ATAS HIJAU
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenDesaPrimary)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Dashboard Pelayanan",
                            style = AppTypography.HeadingLarge.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Admin",
                            style = AppTypography.Caption.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // TOMBOL KELUAR DI HEADER KANAN
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Keluar Akun",
                            tint = Color.White
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // KOLOM PENCARIAN
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Cari nama pemohon atau jenis surat",
                            color = TextPlaceholder,
                            style = AppTypography.BodyRegular
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Cari",
                            tint = TextSecondary
                        )
                    },
                    textStyle = AppTypography.BodyRegular.copy(color = TextPrimary),
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

                Spacer(modifier = Modifier.height(16.dp))

                // FILTER CHIPS ROW DENGAN COUNTER DINAMIS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(filterScrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminFilterChipItem(
                        text = "Semua (${listPengajuan.size})",
                        isSelected = selectedFilter == "Semua",
                        onClick = { selectedFilter = "Semua" }
                    )
                    AdminFilterChipItem(
                        text = "Proses (${listPengajuan.count { it.status == StatusPengajuan.PROSES }})",
                        isSelected = selectedFilter == "Proses",
                        onClick = { selectedFilter = "Proses" }
                    )
                    AdminFilterChipItem(
                        text = "Diterima (${listPengajuan.count { it.status == StatusPengajuan.DITERIMA }})",
                        isSelected = selectedFilter == "Diterima",
                        onClick = { selectedFilter = "Diterima" }
                    )
                    AdminFilterChipItem(
                        text = "Ditolak (${listPengajuan.count { it.status == StatusPengajuan.DITOLAK }})",
                        isSelected = selectedFilter == "Ditolak",
                        onClick = { selectedFilter = "Ditolak" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // JIKA KOSONG
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada pengajuan surat masuk.",
                            style = AppTypography.BodyRegular,
                            color = TextSecondary
                        )
                    }
                } else {
                    // DAFTAR KARTU PENGAJUAN ADMIN REAL-TIME
                    filteredList.forEach { item ->
                        AdminPengajuanCard(
                            item = item,
                            onSelesaikanClicked = { onSelesaikanClicked(item) },
                            onDetailClicked = { onDetailClicked(item) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    // POP-UP DIALOG KONFIRMASI KELUAR AKUN
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
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
                        color = Color(0xFFFEE2E2)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = null,
                                tint = Color(0xFFDC2626)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Keluar dari Akun Admin?",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Text(
                    text = "Anda harus memasukkan email admin kembali untuk mengakses dashboard pelayanan kelurahan.",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClicked()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(
                        text = "Ya, Keluar",
                        style = AppTypography.ButtonText
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
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

@Composable
fun AdminFilterChipItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) GreenDesaPrimary else Color.White,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, GrayBorder),
        modifier = Modifier.height(38.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = AppTypography.Caption.copy(fontWeight = FontWeight.Medium),
                color = if (isSelected) Color.White else TextPrimary
            )
        }
    }
}

@Composable
fun AdminPengajuanCard(
    item: PengajuanItem,
    onSelesaikanClicked: () -> Unit,
    onDetailClicked: () -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = item.status)
                Text(
                    text = item.tanggal,
                    style = AppTypography.Caption,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.jenisSurat,
                style = AppTypography.HeadingMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Nama Pemohon: ${item.namaPemohon}",
                style = AppTypography.BodyRegular,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            val containerColor = when (item.status) {
                StatusPengajuan.PROSES -> OrangeProcessContainer
                StatusPengajuan.DITERIMA -> GreenDesaLight
                StatusPengajuan.DITOLAK -> RedRejectedContainer
            }
            val textColor = when (item.status) {
                StatusPengajuan.PROSES -> OrangeProcessText
                StatusPengajuan.DITERIMA -> GreenDesaPrimary
                StatusPengajuan.DITOLAK -> RedRejectedText
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = containerColor
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    if (item.status == StatusPengajuan.DITOLAK) {
                        Text(
                            text = "Alasan Penolakan Admin:",
                            style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    Text(
                        text = item.pesanStatus,
                        style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.Medium),
                        color = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                // Tombol Lihat Detail
                OutlinedButton(
                    onClick = onDetailClicked,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = GreenDesaLight),
                    border = null,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Lihat Detail",
                        style = AppTypography.Caption.copy(
                            fontWeight = FontWeight.Bold,
                            color = GreenDesaPrimary
                        )
                    )
                }

                // Tombol Selesaikan khusus untuk status PROSES
                if (item.status == StatusPengajuan.PROSES) {
                    Button(
                        onClick = onSelesaikanClicked,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Selesaikan",
                            style = AppTypography.Caption.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}