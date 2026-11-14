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

// Catatan: data class PengajuanItem dan enum StatusPengajuan sudah ada di ModelPengajuan.kt,
// jadi tidak dideklarasikan ulang di sini agar terhindar dari error Redeclaration.

@Composable
fun StatusScreen(
    listPengajuan: List<PengajuanItem> = emptyList(), // 👈 Menerima list data real-time dari MainActivity
    onTabSelected: (Int) -> Unit = {},
    onDetailClicked: (PengajuanItem) -> Unit = {},    // 👈 Mengirimkan objek PengajuanItem lengkap
    onResubmitClicked: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(1) } // Tab Status aktif di indeks 1
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    val scrollState = rememberScrollState()
    val filterScrollState = rememberScrollState()

    // Menghitung jumlah untuk setiap kategori filter berdasarkan data real-time
    val totalCount = listPengajuan.size
    val prosesCount = listPengajuan.count { it.status == StatusPengajuan.PROSES }
    val diterimaCount = listPengajuan.count { it.status == StatusPengajuan.DITERIMA }
    val ditolakCount = listPengajuan.count { it.status == StatusPengajuan.DITOLAK }

    // Logika Filter & Pencarian
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
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda", style = AppTypography.Caption) },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        onTabSelected(0)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenDesaPrimary,
                        selectedTextColor = GreenDesaPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = GreenDesaLight
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Description, contentDescription = "Status") },
                    label = { Text("Status", style = AppTypography.Caption) },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onTabSelected(1)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenDesaPrimary,
                        selectedTextColor = GreenDesaPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = GreenDesaLight
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil", style = AppTypography.Caption) },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onTabSelected(2)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenDesaPrimary,
                        selectedTextColor = GreenDesaPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = GreenDesaLight
                    )
                )
            }
        },
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
                Column {
                    Text(
                        text = "Status Pengajuan",
                        style = AppTypography.HeadingLarge.copy(color = Color.White)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pantau status surat yang telah Anda ajukan",
                        style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.85f))
                    )
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
                    placeholder = { Text("Cari nama pemohon atau jenis surat", color = TextPlaceholder, style = AppTypography.BodyRegular) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari", tint = TextSecondary) },
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

                // FILTER CHIPS ROW DENGAN DYNAMIC COUNTER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(filterScrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChipItem(
                        text = "Semua ($totalCount)",
                        isSelected = selectedFilter == "Semua",
                        onClick = { selectedFilter = "Semua" }
                    )
                    FilterChipItem(
                        text = "Proses ($prosesCount)",
                        isSelected = selectedFilter == "Proses",
                        onClick = { selectedFilter = "Proses" }
                    )
                    FilterChipItem(
                        text = "Diterima ($diterimaCount)",
                        isSelected = selectedFilter == "Diterima",
                        onClick = { selectedFilter = "Diterima" }
                    )
                    FilterChipItem(
                        text = "Ditolak ($ditolakCount)",
                        isSelected = selectedFilter == "Ditolak",
                        onClick = { selectedFilter = "Ditolak" }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // JIKA DAFTAR KOSONG
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada riwayat pengajuan surat.",
                            style = AppTypography.BodyRegular,
                            color = TextSecondary
                        )
                    }
                } else {
                    // DAFTAR KARTU PENGAJUAN REAL-TIME
                    filteredList.forEach { item ->
                        PengajuanCard(
                            item = item,
                            onDetailClicked = { onDetailClicked(item) },
                            onResubmitClicked = { onResubmitClicked(item.jenisSurat) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
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
fun PengajuanCard(
    item: PengajuanItem,
    onDetailClicked: () -> Unit,
    onResubmitClicked: () -> Unit
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

            val containerColor = when(item.status) {
                StatusPengajuan.PROSES -> OrangeProcessContainer
                StatusPengajuan.DITERIMA -> GreenDesaLight
                StatusPengajuan.DITOLAK -> RedRejectedContainer
            }
            val textColor = when(item.status) {
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

            if (item.status == StatusPengajuan.DITOLAK) {
                Button(
                    onClick = onResubmitClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(
                        text = "Ajukan Ulang Surat Ini",
                        style = AppTypography.ButtonText
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onDetailClicked,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = GreenDesaLight),
                    border = null,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Lihat Detail",
                        style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                        color = GreenDesaPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: StatusPengajuan) {
    val (bgColor, textColor, text) = when(status) {
        StatusPengajuan.PROSES -> Triple(OrangeProcessContainer, OrangeProcessText, "• Diproses")
        StatusPengajuan.DITERIMA -> Triple(GreenDesaLight, GreenDesaPrimary, "✓ Diterima")
        StatusPengajuan.DITOLAK -> Triple(RedRejectedContainer, RedRejectedText, "✕ Ditolak")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}