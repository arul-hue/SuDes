package com.sudes.purwodadi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.sudes.purwodadi.ui.theme.SudesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudesTheme {
                var currentScreen by remember { mutableStateOf("splash") }

                // RIWAYAT TAB GLOBAL (Menyimpan urutan tab: "home", "status", "profile")
                val tabHistory = remember { mutableStateListOf("home") }
                var backPressedTime by remember { mutableStateOf(0L) }

                // State untuk menyimpan data surat yang sedang dipilih ke halaman detail
                var selectedJenisSurat by remember { mutableStateOf("Surat Keterangan Domisili") }
                var selectedStatusSurat by remember { mutableStateOf(StatusPengajuan.PROSES) }
                var selectedAlasanDitolak by remember { mutableStateOf<String?>(null) }
                var selectedNamaFileRt by remember { mutableStateOf("pengantar_rt03_sahal.jpg") }

                // State dinamis untuk menampung daftar rincian data inputan surat
                var selectedFields by remember {
                    mutableStateOf(
                        listOf(
                            DetailFieldItem("Nama Lengkap", "Sahal Aw"),
                            DetailFieldItem("NIK", "3501230101900001"),
                            DetailFieldItem("Tempat, Tanggal Lahir", "Bandar Lampung, 01 Januari 1990"),
                            DetailFieldItem("Jenis Kelamin", "Laki-laki"),
                            DetailFieldItem("Agama", "Islam"),
                            DetailFieldItem("Pekerjaan", "Wiraswasta"),
                            DetailFieldItem("Alamat Lengkap", "Jl. Melati No. 12, RT 03/RW 05, Desa Purwodadi Simpang"),
                            DetailFieldItem("Alamat Tempat Tinggal Saat Ini", "Dusun II RT 03, Purwodadi Simpang, Kec. Tanjung Bintang")
                        )
                    )
                }

                val context = this

                // GLOBAL BACKHANDLER UNTUK 3 TAB UTAMA
                if (currentScreen == "home" || currentScreen == "status" || currentScreen == "profile") {
                    BackHandler {
                        if (tabHistory.size > 1) {
                            // Hapus riwayat tab terakhir dengan benar
                            tabHistory.removeAt(tabHistory.size - 1)
                            // Pindah ke tab sebelumnya yang ada di riwayat
                            currentScreen = tabHistory.last()
                        } else {
                            // Jika sudah di akar riwayat (tab pertama), tekan 2x untuk keluar
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - backPressedTime < 2000) {
                                finish()
                            } else {
                                backPressedTime = currentTime
                                Toast.makeText(context, "Tekan sekali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onTimeout = { currentScreen = "login" }
                        )
                    }
                    "login" -> {
                        LoginScreen(
                            onLoginClicked = { email, _ ->
                                // Validasi role berdasarkan email
                                if (email.equals("admin@purwodadi.desa.id", ignoreCase = true) ||
                                    email.contains("admin", ignoreCase = true)) {
                                    currentScreen = "admin_dashboard"
                                } else {
                                    currentScreen = "home"
                                }
                                tabHistory.clear()
                                tabHistory.add(currentScreen)
                            },
                            onGoogleLoginClicked = {
                                currentScreen = "home"
                                tabHistory.clear()
                                tabHistory.add("home")
                            },
                            onForgotPasswordClicked = { currentScreen = "forgot_password" },
                            onRegisterClicked = { currentScreen = "register" }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            onRegisterSubmitted = { _, _, _, _ -> currentScreen = "login" },
                            onGoogleRegisterClicked = {
                                currentScreen = "home"
                                tabHistory.clear()
                                tabHistory.add("home")
                            },
                            onBackToLoginClicked = { currentScreen = "login" }
                        )
                    }
                    "forgot_password" -> {
                        ForgotPasswordScreen(
                            onSubmitEmailClicked = { _ -> currentScreen = "login" },
                            onBackToLoginClicked = { currentScreen = "login" }
                        )
                    }
                    "admin_dashboard" -> {
                        AdminDashboardScreen(
                            onDetailClicked = { jenisSurat ->
                                // Opsional
                            },
                            onSelesaikanClicked = { idPengajuan ->
                                // Ketika tombol Selesai diklik, buka laman edit/koreksi admin
                                currentScreen = "admin_edit_pengajuan"
                            },
                            onLogoutClicked = {
                                // Arahkan kembali ke halaman login dan bersihkan riwayat
                                currentScreen = "login"
                                tabHistory.clear()
                            }
                        )
                    }
                    "admin_edit_pengajuan" -> {
                        AdminEditPengajuanScreen(
                            onBackClicked = { currentScreen = "admin_dashboard" },
                            onSaveSuccess = { currentScreen = "admin_dashboard" }
                        )
                    }
                    "home" -> {
                        HomeScreen(
                            userName = "Sahal",
                            onServiceSelected = { serviceName ->
                                when (serviceName) {
                                    "Surat Keterangan Usaha" -> currentScreen = "surat_usaha"
                                    "Surat Keterangan Domisili" -> currentScreen = "surat_domisili"
                                    "Surat Keterangan Kehilangan" -> currentScreen = "surat_kehilangan"
                                    "Surat Keterangan Kematian" -> currentScreen = "surat_kematian"
                                    "Surat Keterangan Tidak Mampu" -> currentScreen = "surat_sktm"
                                }
                            },
                            onTabSelected = { tabIndex ->
                                val target = when (tabIndex) {
                                    1 -> "status"
                                    2 -> "profile"
                                    else -> "home"
                                }
                                if (currentScreen != target) {
                                    currentScreen = target
                                    tabHistory.add(target) // Catat perpindahan tab ke riwayat global
                                }
                            }
                        )
                    }
                    "status" -> {
                        StatusScreen(
                            onTabSelected = { tabIndex ->
                                val target = when (tabIndex) {
                                    0 -> "home"
                                    2 -> "profile"
                                    else -> "status"
                                }
                                if (currentScreen != target) {
                                    currentScreen = target
                                    tabHistory.add(target)
                                }
                            },
                            onDetailClicked = { jenisSurat ->
                                selectedJenisSurat = jenisSurat

                                when {
                                    jenisSurat.contains("Domisili", ignoreCase = true) -> {
                                        selectedStatusSurat = StatusPengajuan.PROSES
                                        selectedAlasanDitolak = null
                                        selectedNamaFileRt = "pengantar_rt03_sahal.jpg"
                                        selectedFields = listOf(
                                            DetailFieldItem("Nama Lengkap", "Sahal Aw"),
                                            DetailFieldItem("NIK", "3501230101900001"),
                                            DetailFieldItem("Tempat, Tanggal Lahir", "Bandar Lampung, 01 Januari 1990"),
                                            DetailFieldItem("Jenis Kelamin", "Laki-laki"),
                                            DetailFieldItem("Agama", "Islam"),
                                            DetailFieldItem("Pekerjaan", "Wiraswasta"),
                                            DetailFieldItem("Alamat Lengkap", "Jl. Melati No. 12, RT 03/RW 05, Desa Purwodadi Simpang"),
                                            DetailFieldItem("Alamat Tempat Tinggal Saat Ini", "Dusun II RT 03, Purwodadi Simpang, Kec. Tanjung Bintang")
                                        )
                                    }
                                    jenisSurat.contains("Usaha", ignoreCase = true) -> {
                                        selectedStatusSurat = StatusPengajuan.DITERIMA
                                        selectedAlasanDitolak = null
                                        selectedNamaFileRt = "pengantar_rt03_roy.jpg"
                                        selectedFields = listOf(
                                            DetailFieldItem("Nama Lengkap", "Roy UX"),
                                            DetailFieldItem("NIK", "3501230101900002"),
                                            DetailFieldItem("Tempat, Tanggal Lahir", "Bandar Lampung, 12 Mei 1992"),
                                            DetailFieldItem("Jenis Kelamin", "Laki-laki"),
                                            DetailFieldItem("Agama", "Islam"),
                                            DetailFieldItem("Status Perkawinan", "Kawin"),
                                            DetailFieldItem("Pekerjaan", "Wiraswasta"),
                                            DetailFieldItem("Alamat Lengkap", "Jl. Melati No. 12, RT 03/RW 05, Desa Purwodadi Simpang"),
                                            DetailFieldItem("Nama / Deskripsi Usaha", "Warung Sembako Berkah"),
                                            DetailFieldItem("Lokasi Usaha", "Dusun II RT 03, Pasar Desa, Purwodadi Simpang")
                                        )
                                    }
                                    jenisSurat.contains("Tidak Mampu", ignoreCase = true) || jenisSurat.contains("SKTM", ignoreCase = true) -> {
                                        selectedStatusSurat = StatusPengajuan.DITOLAK
                                        selectedAlasanDitolak = "Foto surat pengantar RT buram dan tanda tangan Ketua RT belum terlihat jelas. Mohon unggah ulang."
                                        selectedNamaFileRt = "pengantar_syahrul.jpg"
                                        selectedFields = listOf(
                                            DetailFieldItem("Nama Lengkap", "Syahrul Rek"),
                                            DetailFieldItem("NIK", "3501230202900002"),
                                            DetailFieldItem("Tempat, Tanggal Lahir", "Bandar Lampung, 15 Mei 1988"),
                                            DetailFieldItem("Jenis Kelamin", "Laki-laki"),
                                            DetailFieldItem("Agama", "Islam"),
                                            DetailFieldItem("Status Perkawinan", "Belum Kawin"),
                                            DetailFieldItem("Pekerjaan", "Mahasiswa"),
                                            DetailFieldItem("Alamat Lengkap", "Jl. Anggrek No. 5, RT 01/RW 02, Desa Purwodadi Simpang"),
                                            DetailFieldItem("Nomor Kartu Keluarga (KK)", "3501239908120001"),
                                            DetailFieldItem("Nama Kepala Keluarga (di KK)", "Budi Santoso"),
                                            DetailFieldItem("Keperluan Pembuatan Surat", "Keringanan UKT Kuliah")
                                        )
                                    }
                                }
                                currentScreen = "detail_pengajuan"
                            },
                            onResubmitClicked = { jenisSurat ->
                                when {
                                    jenisSurat.contains("Usaha", ignoreCase = true) -> currentScreen = "surat_usaha"
                                    jenisSurat.contains("Domisili", ignoreCase = true) -> currentScreen = "surat_domisili"
                                    jenisSurat.contains("Tidak Mampu", ignoreCase = true) || jenisSurat.contains("SKTM", ignoreCase = true) -> currentScreen = "surat_sktm"
                                    else -> currentScreen = "home"
                                }
                            }
                        )
                    }
                    "detail_pengajuan" -> {
                        DetailPengajuanScreen(
                            jenisSurat = selectedJenisSurat,
                            status = selectedStatusSurat,
                            alasanDitolak = selectedAlasanDitolak,
                            namaFileRt = selectedNamaFileRt,
                            fields = selectedFields,
                            onBackClicked = { currentScreen = "status" },
                            onResubmitClicked = { jenisSurat ->
                                when {
                                    jenisSurat.contains("Usaha", ignoreCase = true) -> currentScreen = "surat_usaha"
                                    jenisSurat.contains("Domisili", ignoreCase = true) -> currentScreen = "surat_domisili"
                                    jenisSurat.contains("Tidak Mampu", ignoreCase = true) || jenisSurat.contains("SKTM", ignoreCase = true) -> currentScreen = "surat_sktm"
                                    else -> currentScreen = "home"
                                }
                            }
                        )
                    }
                    "profile" -> {
                        ProfileScreen(
                            userName = "Sahal",
                            userFullName = "Sahal Alfarizi",
                            onTabSelected = { tabIndex ->
                                val target = when (tabIndex) {
                                    0 -> "home"
                                    1 -> "status"
                                    else -> "profile"
                                }
                                if (currentScreen != target) {
                                    currentScreen = target
                                    tabHistory.add(target) // Catat perpindahan tab ke riwayat global
                                }
                            },
                            onEditClicked = { currentScreen = "edit_profile" },
                            onLogoutClicked = {
                                currentScreen = "login"
                                tabHistory.clear()
                            },
                            onUbahEmailClicked = { currentScreen = "ubah_email" },
                            onGantiNoHpClicked = { currentScreen = "ganti_nohp" },
                            onGantiPasswordClicked = { currentScreen = "ganti_password" },
                            onVideoTutorialClicked = { currentScreen = "video_tutorial" },
                            onBantuanFaqClicked = { currentScreen = "bantuan_faq" },
                            onTentangAplikasiClicked = { currentScreen = "tentang_aplikasi" }
                        )
                    }
                    "edit_profile" -> {
                        EditProfileScreen(
                            initialNama = "Sahal Alfarizi",
                            onBackClicked = { currentScreen = "profile" },
                            onSaveSuccess = { currentScreen = "profile" }
                        )
                    }
                    "ubah_email" -> {
                        UbahEmailScreen(
                            onBackClicked = { currentScreen = "profile" },
                            onSaveSuccess = { currentScreen = "profile" }
                        )
                    }
                    "ganti_nohp" -> {
                        GantiNoHpScreen(
                            onBackClicked = { currentScreen = "profile" },
                            onSaveSuccess = { currentScreen = "profile" }
                        )
                    }
                    "ganti_password" -> {
                        GantiPasswordScreen(
                            onBackClicked = { currentScreen = "profile" },
                            onSaveSuccess = { currentScreen = "profile" }
                        )
                    }
                    "video_tutorial" -> {
                        VideoTutorialScreen(onBackClicked = { currentScreen = "profile" })
                    }
                    "bantuan_faq" -> {
                        BantuanFaqScreen(onBackClicked = { currentScreen = "profile" })
                    }
                    "tentang_aplikasi" -> {
                        TentangAplikasiScreen(onBackClicked = { currentScreen = "profile" })
                    }
                    "surat_usaha" -> {
                        SuratUsahaScreen(
                            onBackClicked = { currentScreen = "home" },
                            onSubmitSuccess = { currentScreen = "home" }
                        )
                    }
                    "surat_domisili" -> {
                        SuratDomisiliScreen(
                            onBackClicked = { currentScreen = "home" },
                            onSubmitSuccess = { currentScreen = "home" }
                        )
                    }
                    "surat_kehilangan" -> {
                        SuratKehilanganScreen(
                            onBackClicked = { currentScreen = "home" },
                            onSubmitSuccess = { currentScreen = "home" }
                        )
                    }
                    "surat_kematian" -> {
                        SuratKematianScreen(
                            onBackClicked = { currentScreen = "home" },
                            onSubmitSuccess = { currentScreen = "home" }
                        )
                    }
                    "surat_sktm" -> {
                        SuratTidakMampuScreen(
                            onBackClicked = { currentScreen = "home" },
                            onSubmitSuccess = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}