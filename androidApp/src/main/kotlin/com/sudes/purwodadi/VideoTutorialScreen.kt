package com.sudes.purwodadi

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.sudes.purwodadi.ui.theme.*

data class VideoTutorialItem(
    val title: String,
    val description: String,
    val duration: String,
    val videoUrl: String // 💡 Ditambahkan link video untuk masing-masing item
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTutorialScreen(
    onBackClicked: () -> Unit = {}
) {
    // State untuk melacak video mana yang sedang dipilih/diputar (null jika sedang di halaman daftar)
    var selectedVideo by remember { mutableStateOf<VideoTutorialItem?>(null) }

    val videoList = listOf(
        VideoTutorialItem(
            "Cara Mengajukan Surat Domisili",
            "Panduan lengkap langkah demi langkah",
            "03:45 mnt",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        VideoTutorialItem(
            "Melengkapi Data Diri & Auto-Fill",
            "Agar formulir surat otomatis terisi",
            "02:30 mnt",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        ),
        VideoTutorialItem(
            "Mengecek Status & Ambil Surat",
            "Panduan mengambil surat di balai desa",
            "04:15 mnt",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        ),
        VideoTutorialItem(
            "Berkas Ditolak & Ajukan Ulang",
            "Cara memperbaiki berkas yang keliru",
            "03:10 mnt",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        )
    )

    BackHandler {
        if (selectedVideo != null) {
            // Jika sedang memutar video, kembali ke daftar video
            selectedVideo = null
        } else {
            // Jika di halaman daftar, kembali ke halaman sebelumnya (profil/beranda)
            onBackClicked()
        }
    }

    // JIKA ADA VIDEO YANG DIPILIH, TAMPILKAN HALAMAN PEMUTAR VIDEO (PLAYER)
    if (selectedVideo != null) {
        VideoPlayerDetailScreen(
            video = selectedVideo!!,
            onBackClicked = { selectedVideo = null }
        )
        return
    }

    // JIKA TIDAK, TAMPILKAN DAFTAR MENU VIDEO TUTORIAL
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
                VideoCardItem(
                    video = video,
                    onClick = { selectedVideo = video } // 💡 Saat diklik, buka pemutar video
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun VideoCardItem(video: VideoTutorialItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick), // 💡 Membuat card bisa diklik
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

// 💡 KOMPONEN PEMUTAR VIDEO DENGAN MEDIA3 EXOPLAYER (BAR SKIP, PLAY/PAUSE, DLL)
@Composable
fun VideoPlayerDetailScreen(
    video: VideoTutorialItem,
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(video.videoUrl))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true // Langsung putar saat dibuka
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
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
                    IconButton(onClick = {
                        exoPlayer.release()
                        onBackClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Putar Video Tutorial",
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Kotak Layar Pemutar Video
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black,
                shadowElevation = 4.dp
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true // Menampilkan bar skip, play/pause, durasi
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Informasi Detail Video yang Dipilih
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = video.title,
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = video.description,
                        style = AppTypography.BodyRegular,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}