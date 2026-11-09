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

// Menggunakan model PengajuanSurat yang sudah ada di SuratViewModel.kt
// (Pastikan enum StatusPengajuan dicocokkan atau dikonversi ke String dari Firestore)

@Composable
fun StatusScreen(
    onTabSelected: (Int) -> Unit = {},
    onDetailClicked: (String) -> Unit = {},
    onResubmitClicked: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(1) } // Tab Status aktif di indeks 1
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    val scrollState = rememberScrollState()
    val filterScrollState = rememberScrollState()

    // Hubungkan ke SuratViewModel untuk mengambil data real-time dari Firestore
    val suratViewModel = remember { SuratViewModel() }
    val listPengajuan by suratViewModel.daftarSuratWarga.collectAsState()
    val isLoading by suratViewModel.isLoading.collectAsState()

    // Panggil fungsi muat riwayat saat pertama kali layar dibuka
    LaunchedEffect(Unit) {
        suratViewModel.muatRiwayatSuratWarga()
    }

    // Hitung jumlah untuk chip filter berdasarkan data asli Firestore
    val totalCount = listPengajuan.size
    val prosesCount = listPengajuan.count { it.status.equals("Proses", ignoreCase = true) }
    val diterimaCount = listPengajuan.count { it.status.equals("Diterima", ignoreCase = true) }
    val ditolakCount = listPengajuan.count { it.status.equals("Ditolak", ignoreCase = true) }

    // Logika Filter & Pencarian berdasarkan data Firestore
    val filteredList = listPengajuan.filter { item ->
        val matchSearch = item.jenisSurat.contains(searchQuery, ignoreCase = true) ||
                item.namaLengkap.contains(searchQuery, ignoreCase = true)
        val matchFilter = when (selectedFilter) {
            "Proses" -> item.status.equals("Proses", ignoreCase = true)
            "Diterima" -> item.status.equals("Diterima", ignoreCase = true)
            "Ditolak" -> item.status.equals("Ditolak", ignoreCase = true)
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

                // FILTER CHIPS ROW DENGAN DINAMIS COUNTER
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

                // KONDISI LOADING ATAU KOSONG
                if (isLoading && listPengajuan.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenDesaPrimary)
                    }
                } else if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada riwayat pengajuan surat.",
                            style = AppTypography.BodyRegular,
                            color = TextSecondary
                        )
                    }
                } else {
                    // DAFTAR KARTU PENGAJUAN DARI FIRESTORE
                    filteredList.forEach { item ->
                        PengajuanFirestoreCard(
                            item = item,
                            onDetailClicked = { onDetailClicked(item.jenisSurat) },
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
fun PengajuanFirestoreCard(
    item: PengajuanSurat,
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
                StatusFirestoreBadge(status = item.status)
                Text(
                    text = item.tanggalPengajuan,
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
                text = "Nama Pemohon: ${item.namaLengkap}",
                style = AppTypography.BodyRegular,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            val isProses = item.status.equals("Proses", ignoreCase = true)
            val isDiterima = item.status.equals("Diterima", ignoreCase = true)

            val containerColor = when {
                isProses -> OrangeProcessContainer
                isDiterima -> GreenDesaLight
                else -> RedRejectedContainer
            }
            val textColor = when {
                isProses -> OrangeProcessText
                isDiterima -> GreenDesaPrimary
                else -> RedRejectedText
            }

            val pesanTampil = when {
                isProses -> "Berkas sedang dicek perangkat desa"
                isDiterima -> "Surat dapat diambil di Balai Desa. No. Surat: ${item.nomorSurat}"
                else -> item.alasanPenolakan.ifEmpty { "Pengajuan ditolak. Mohon unggah ulang dokumen." }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = containerColor
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    if (!isProses && !isDiterima) {
                        Text(
                            text = "Alasan Penolakan Admin:",
                            style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    Text(
                        text = pesanTampil,
                        style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.Medium),
                        color = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!isProses && !isDiterima) {
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
fun StatusFirestoreBadge(status: String) {
    val (bgColor, textColor, text) = when {
        status.equals("Proses", ignoreCase = true) -> Triple(OrangeProcessContainer, OrangeProcessText, "• Diproses")
        status.equals("Diterima", ignoreCase = true) -> Triple(GreenDesaLight, GreenDesaPrimary, "✓ Diterima")
        else -> Triple(RedRejectedContainer, RedRejectedText, "✕ Ditolak")
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