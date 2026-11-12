package com.sudes.purwodadi

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
fun ProfileScreen(
    namaLengkap: String = "Warga", // Murni menggunakan namaLengkap
    userRole: String = "Warga Desa Purwodadi Simpang",
    // Parameter data diri dinamis
    nik: String = "-",
    tempatTanggalLahir: String = "-",
    pekerjaan: String = "-",
    agama: String = "-",
    statusPerkawinan: String = "-",
    alamatLengkap: String = "-",

    onTabSelected: (Int) -> Unit = {},
    onEditClicked: () -> Unit = {},
    onLogoutClicked: () -> Unit = {},
    onUbahEmailClicked: () -> Unit = {},
    onGantiNoHpClicked: () -> Unit = {},
    onGantiPasswordClicked: () -> Unit = {},
    onVideoTutorialClicked: () -> Unit = {},
    onBantuanFaqClicked: () -> Unit = {},
    onTentangAplikasiClicked: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(2) } // Tab Profil aktif di indeks ke-2
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) } // State pop-up keluar akun

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
            // HEADER PROFIL (Avatar & Info Warga)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenDesaPrimary)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(52.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = namaLengkap, // Menampilkan nama asli dari database
                            style = AppTypography.HeadingLarge.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = userRole,
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.85f))
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // KARTU 1: DATA DIRI
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DATA DIRI",
                                style = AppTypography.HeadingMedium,
                                color = TextPrimary
                            )
                            IconButton(onClick = onEditClicked, modifier = Modifier.size(24.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Data Diri",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderColor, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileDataRow(label = "Nama", value = namaLengkap)
                        ProfileDataRow(label = "NIK", value = nik)
                        ProfileDataRow(label = "Tempat, Tgl Lahir", value = tempatTanggalLahir)
                        ProfileDataRow(label = "Pekerjaan", value = pekerjaan)
                        ProfileDataRow(label = "Agama", value = agama)
                        ProfileDataRow(label = "Status", value = statusPerkawinan)
                        ProfileDataRow(label = "Alamat", value = alamatLengkap)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 2: PENGATURAN AKUN
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
                            text = "PENGATURAN AKUN",
                            style = AppTypography.HeadingMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderColor, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(4.dp))

                        ProfileMenuItem(title = "Ubah Email Google", onClick = onUbahEmailClicked)
                        ProfileMenuItem(title = "Ganti Nomor Handphone", onClick = onGantiNoHpClicked)
                        ProfileMenuItem(title = "Ganti Password", onClick = onGantiPasswordClicked)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KARTU 3: BANTUAN & PANDUAN
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
                            text = "BANTUAN & PANDUAN",
                            style = AppTypography.HeadingMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderColor, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(4.dp))

                        ProfileMenuItem(title = "Video Tutorial SuDes", onClick = onVideoTutorialClicked)
                        ProfileMenuItem(title = "Bantuan & Pertanyaan Umum", onClick = onBantuanFaqClicked)
                        ProfileMenuItem(title = "Tentang Aplikasi SuDes", onClick = onTentangAplikasiClicked)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // TOMBOL KELUAR DARI AKUN
                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFFEF2F2)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Keluar dari Akun",
                        style = AppTypography.ButtonText.copy(color = Color(0xFFDC2626))
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
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
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = Color(0xFFFEF2F2)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Keluar dari Akun?", style = AppTypography.HeadingMedium, color = TextPrimary)
                }
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin keluar dari aplikasi Desa Purwodadi Simpang?",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogoutClicked()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text(text = "Ya, Keluar", style = AppTypography.ButtonText.copy(color = Color.White))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { showLogoutDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Batal", color = TextPrimary, style = AppTypography.ButtonText)
                    }
                }
            },
            dismissButton = {}
        )
    }
}

// Komponen Baris Data Diri (Label : Value)
@Composable
fun ProfileDataRow(label: String, value: String) {
    val displayValue = if (value.isBlank() || value == "-") "Belum diisi" else value
    val isPlaceholder = value.isBlank() || value == "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = AppTypography.BodyRegular.copy(color = TextSecondary),
            modifier = Modifier.width(120.dp)
        )
        Row(modifier = Modifier.weight(1f)) {
            Text(
                text = ": ",
                style = AppTypography.BodyRegular.copy(color = TextSecondary)
            )
            Text(
                text = displayValue,
                style = AppTypography.BodyRegular.copy(
                    color = if (isPlaceholder) TextPlaceholder else TextPrimary,
                    fontWeight = if (isPlaceholder) FontWeight.Normal else FontWeight.Medium
                )
            )
        }
    }
}

// Komponen Item Menu dengan Chevron Kanan
@Composable
fun ProfileMenuItem(title: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = AppTypography.BodyRegular.copy(color = TextPrimary)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Detail",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}