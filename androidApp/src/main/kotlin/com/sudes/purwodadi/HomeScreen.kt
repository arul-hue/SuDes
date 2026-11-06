package com.sudes.purwodadi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.sp
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
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        onTabSelected(0)
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda", style = AppTypography.Caption) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenDesaPrimary,
                        selectedTextColor = GreenDesaPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onTabSelected(1)
                    },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Status") },
                    label = { Text("Status", style = AppTypography.Caption) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenDesaPrimary,
                        selectedTextColor = GreenDesaPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onTabSelected(2)
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil", style = AppTypography.Caption) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenDesaPrimary,
                        selectedTextColor = GreenDesaPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenDesaPrimary)
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Avatar",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Selamat Datang,",
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = userName,
                            style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 20.sp)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GreenDesaPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Layanan Surat Online Desa",
                        style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pilih jenis surat yang ingin Anda ajukan di bawah ini",
                        style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Layanan Surat Keterangan",
                    style = AppTypography.HeadingLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Usaha",
                    description = "Pengajuan surat izin usaha desa",
                    icon = Icons.Default.BusinessCenter,
                    onClick = { onServiceSelected("Surat Keterangan Usaha") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Domisili",
                    description = "Bukti tempat tinggal tetap/sementara",
                    icon = Icons.Default.LocationOn,
                    onClick = { onServiceSelected("Surat Keterangan Domisili") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Kehilangan",
                    description = "Surat pengantar kehilangan dokumen/barang",
                    icon = Icons.Default.Search,
                    onClick = { onServiceSelected("Surat Keterangan Kehilangan") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Kematian",
                    description = "Pelaporan data keterangan meninggal dunia",
                    icon = Icons.Default.Description,
                    onClick = { onServiceSelected("Surat Keterangan Kematian") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ServiceItemCard(
                    title = "Surat Keterangan Tidak Mampu",
                    description = "Pengajuan SKTM untuk bantuan/pendidikan",
                    icon = Icons.Default.AssignmentReturned,
                    onClick = { onServiceSelected("Surat Keterangan Tidak Mampu") }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ServiceItemCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(10.dp),
                color = GreenDesaLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = GreenDesaPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = AppTypography.HeadingLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = AppTypography.BodyRegular.copy(fontSize = 11.sp),
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