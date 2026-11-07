package com.sudes.purwodadi

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@Composable
fun HomeScreen(
    userName: String = "Sahal",
    onServiceSelected: (String) -> Unit = {},
    onTabSelected: (Int) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

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
            // HEADER BERANDA (Avatar di kiri, teks di kanan)
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
                            text = "Selamat Datang,",
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.85f))
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = userName,
                            style = AppTypography.HeadingLarge.copy(color = Color.White)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // KARTU BANNER HIJAU: Layanan Surat Online Desa
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = GreenDesaPrimary
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Layanan Surat Online Desa",
                            style = AppTypography.HeadingMedium.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pilih jenis surat yang ingin Anda ajukan di bawah ini",
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // SECTION TITLE: Layanan Surat Keterangan
                Text(
                    text = "Lima Layanan Surat Keterangan",
                    style = AppTypography.HeadingMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // DAFTAR 5 MENU LAYANAN SURAT
                ServiceItemCard(
                    title = "Surat Keterangan Usaha",
                    subtitle = "Pengajuan surat izin usaha desa",
                    icon = Icons.Default.BusinessCenter,
                    onClick = { onServiceSelected("Surat Keterangan Usaha") }
                )
                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Domisili",
                    subtitle = "Bukti tempat tinggal tetap/sementara",
                    icon = Icons.Default.LocationOn,
                    onClick = { onServiceSelected("Surat Keterangan Domisili") }
                )
                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Kehilangan",
                    subtitle = "Surat pengantar kehilangan dokumen/barang",
                    icon = Icons.Default.Search,
                    onClick = { onServiceSelected("Surat Keterangan Kehilangan") }
                )
                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Kematian",
                    subtitle = "Pelaporan data keterangan meninggal dunia",
                    icon = Icons.Default.Description,
                    onClick = { onServiceSelected("Surat Keterangan Kematian") }
                )
                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Tidak Mampu",
                    subtitle = "Pengajuan SKTM untuk bantuan/pendidikan",
                    icon = Icons.Default.Assignment,
                    onClick = { onServiceSelected("Surat Keterangan Tidak Mampu") }
                )
            }
        }
    }
}

// Komponen Kartu Item Layanan
@Composable
fun ServiceItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(10.dp),
                color = GreenDesaLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GreenDesaPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = AppTypography.HeadingMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = AppTypography.BodyRegular,
                    color = TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Detail",
                tint = TextSecondary
            )
        }
    }
}