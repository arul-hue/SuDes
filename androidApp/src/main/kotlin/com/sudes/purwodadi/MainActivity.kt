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
                val context = this

                // GLOBAL BACKHANDLER UNTUK 3 TAB UTAMA
                if (currentScreen == "home" || currentScreen == "status" || currentScreen == "profile") {
                    BackHandler {
                        if (tabHistory.size > 1) {
                            // Hapus riwayat tab terakhir
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
                            onLoginClicked = { _, _ ->
                                currentScreen = "home"
                                tabHistory.clear()
                                tabHistory.add("home")
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
                                    tabHistory.add(target) // Catat perpindahan tab ke riwayat global
                                }
                            },
                            onDetailClicked = { _ ->
                                // Aksi lihat detail surat jika ada
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
                            onTentangAplikasiClicked = { currentScreen = "tentang_aplikasi" },
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