package com.sudes.purwodadi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

data class VideoTutorialItem(
    val title: String,
    val description: String,
    val duration: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTutorialScreen(
    onBackClicked: () -> Unit = {}
) {
    val videoList = listOf(
        VideoTutorialItem("Cara Mengajukan Surat Domisili", "Panduan lengkap langkah demi langkah", "03:45 mnt"),
        VideoTutorialItem("Melengkapi Data Diri & Auto-Fill", "Agar formulir surat otomatis terisi", "02:30 mnt"),
        VideoTutorialItem("Mengecek Status & Ambil Surat", "Panduan mengambil surat di balai desa", "04:15 mnt"),
        VideoTutorialItem("Berkas Ditolak & Ajukan Ulang", "Cara memperbaiki berkas yang keliru", "03:10 mnt")
    )

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
                        text = "Video Tutorial SuDes",
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Pilih video panduan untuk mulai menonton:",
                style = AppTypography.BodyRegular,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            videoList.forEach { video ->
                VideoCardItem(video = video)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun VideoCardItem(video: VideoTutorialItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail dengan tombol play
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .height(64.dp)
                    .background(Color(0xFFE2E8F0), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = GreenDesaPrimary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    style = AppTypography.HeadingMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = video.description,
                    style = AppTypography.BodyRegular,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GreenDesaLight
                ) {
                    Text(
                        text = video.duration,
                        style = AppTypography.Caption.copy(color = GreenDesaPrimary),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}