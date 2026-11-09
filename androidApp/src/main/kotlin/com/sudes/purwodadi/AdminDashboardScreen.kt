package com.sudes.purwodadi

import androidx.compose.foundation.background
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
    onDetailSuratClicked: (String) -> Unit = {},
    onLogoutClicked: () -> Unit = {}
) {
    val suratViewModel = remember { SuratViewModel() }
    val listPengajuanAdmin by suratViewModel.daftarSuratAdmin.collectAsState()
    val isLoading by suratViewModel.isLoading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }
    val scrollState = rememberScrollState()

    // Muat semua surat untuk admin saat pertama kali dibuka
    LaunchedEffect(Unit) {
        suratViewModel.muatSemuaSuratUntukAdmin()
    }

    val totalCount = listPengajuanAdmin.size
    val prosesCount = listPengajuanAdmin.count { it.status.equals("Proses", ignoreCase = true) }
    val diterimaCount = listPengajuanAdmin.count { it.status.equals("Diterima", ignoreCase = true) }
    val ditolakCount = listPengajuanAdmin.count { it.status.equals("Ditolak", ignoreCase = true) }

    val filteredList = listPengajuanAdmin.filter { item ->
        val matchSearch = item.jenisSurat.contains(searchQuery, ignoreCase = true) ||
                item.namaLengkap.contains(searchQuery, ignoreCase = true) ||
                item.nik.contains(searchQuery, ignoreCase = true)
        val matchFilter = when (selectedFilter) {
            "Proses" -> item.status.equals("Proses", ignoreCase = true)
            "Diterima" -> item.status.equals("Diterima", ignoreCase = true)
            "Ditolak" -> item.status.equals("Ditolak", ignoreCase = true)
            else -> true
        }
        matchSearch && matchFilter
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
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard Admin Desa",
                        style = AppTypography.HeadingMedium.copy(color = Color.White)
                    )
                    IconButton(onClick = onLogoutClicked) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Keluar",
                            tint = Color.White
                        )
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
            // Kotak Pencarian
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari warga atau jenis surat...", color = TextPlaceholder, style = AppTypography.BodyRegular) },
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

            // Ringkasan Statistik
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(title = "Total", count = totalCount, color = GreenDesaPrimary, modifier = Modifier.weight(1f))
                AdminStatCard(title = "Proses", count = prosesCount, color = Color(0xFFD97706), modifier = Modifier.weight(1f))
                AdminStatCard(title = "Diterima", count = diterimaCount, color = Color(0xFF059669), modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading && listPengajuanAdmin.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenDesaPrimary)
                }
            } else if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Belum ada data pengajuan surat.", style = AppTypography.BodyRegular, color = TextSecondary)
                }
            } else {
                filteredList.forEach { item ->
                    AdminPengajuanCard(
                        item = item,
                        onVerifyClicked = { diterima, nomorResmi, alasan ->
                            suratViewModel.verifikasiSuratAdmin(
                                suratId = item.id,
                                nomorSuratResmi = nomorResmi,
                                diterima = diterima,
                                alasanPenolakan = alasan,
                                onSuccess = {},
                                onError = {}
                            )
                        },
                        onDetailClicked = { onDetailSuratClicked(item.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(title: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = AppTypography.Caption, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = count.toString(), style = AppTypography.HeadingMedium, color = color)
        }
    }
}

@Composable
fun AdminPengajuanCard(
    item: PengajuanSurat,
    onVerifyClicked: (Boolean, String, String) -> Unit,
    onDetailClicked: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var nomorResmiInput by remember { mutableStateOf("") }
    var alasanTolakInput by remember { mutableStateOf("") }
    var isAksiTerima by remember { mutableStateOf(true) }

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
                Text(text = item.tanggalPengajuan, style = AppTypography.Caption, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = item.jenisSurat, style = AppTypography.HeadingMedium, color = TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Pemohon: ${item.namaLengkap} (NIK: ${item.nik})", style = AppTypography.BodyRegular, color = TextSecondary)
            Text(text = "Keperluan: ${item.keperluanSurat}", style = AppTypography.BodyRegular, color = TextSecondary)

            Spacer(modifier = Modifier.height(14.dp))

            if (item.status.equals("Proses", ignoreCase = true)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            isAksiTerima = true
                            showDialog = true
                        },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                    ) {
                        Text(text = "Terima", style = AppTypography.ButtonText)
                    }

                    Button(
                        onClick = {
                            isAksiTerima = false
                            showDialog = true
                        },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text(text = "Tolak", style = AppTypography.ButtonText)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Alignment.End.let { Arrangement.End }
            ) {
                OutlinedButton(
                    onClick = onDetailClicked,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = GreenDesaLight),
                    border = null,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(text = "Lihat Detail Lengkap", style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold), color = GreenDesaPrimary)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = if (isAksiTerima) "Terima Pengajuan Surat" else "Tolak Pengajuan Surat") },
            text = {
                Column {
                    if (isAksiTerima) {
                        Text(text = "Masukkan Nomor Surat Resmi Desa:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = nomorResmiInput,
                            onValueChange = { nomorResmiInput = it },
                            placeholder = { Text("Contoh: 400/123/SKTM/2026") },
                            singleLine = true
                        )
                    } else {
                        Text(text = "Masukkan Alasan Penolakan:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = alasanTolakInput,
                            onValueChange = { alasanTolakInput = it },
                            placeholder = { Text("Contoh: Foto pengantar RT kurang jelas") }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onVerifyClicked(isAksiTerima, nomorResmiInput, alasanTolakInput)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text("Konfirmasi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}