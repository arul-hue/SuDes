package com.sudes.purwodadi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.sudes.purwodadi.ui.theme.SudesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudesTheme {
                // State untuk mengatur halaman aktif saat ini
                var currentScreen by remember { mutableStateOf("splash") }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onTimeout = { currentScreen = "login" }
                        )
                    }
                    "login" -> {
                        LoginScreen(
                            onLoginClicked = { _, _ -> currentScreen = "home" },
                            onGoogleLoginClicked = { currentScreen = "home" },
                            onForgotPasswordClicked = { currentScreen = "forgot_password" },
                            onRegisterClicked = { currentScreen = "register" }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            onRegisterSubmitted = { _, _, _, _ -> currentScreen = "login" },
                            onGoogleRegisterClicked = { currentScreen = "home" },
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
                                // Routing ketika menu surat diklik
                                when (serviceName) {
                                    "Surat Keterangan Usaha" -> currentScreen = "surat_usaha"
                                    "Surat Keterangan Domisili" -> currentScreen = "surat_domisili"
                                    "Surat Keterangan Kehilangan" -> currentScreen = "surat_kehilangan"
                                    "Surat Keterangan Kematian" -> currentScreen = "surat_kematian"
                                    "Surat Keterangan Tidak Mampu" -> currentScreen = "surat_sktm"
                                }
                            },
                            onTabSelected = { tabIndex ->
                                // Routing dari Bottom Navigation Bar di Beranda
                                if (tabIndex == 2) {
                                    currentScreen = "profile"
                                }
                            }
                        )
                    }
                    "profile" -> {
                        ProfileScreen(
                            userName = "Sahal",
                            userFullName = "Sahal Alfarizi",
                            onTabSelected = { tabIndex ->
                                if (tabIndex == 0) {
                                    currentScreen = "home"
                                }
                            },
                            onEditClicked = {
                                currentScreen = "edit_profile" // Menuju ke halaman edit data diri[cite: 12]
                            },
                            onLogoutClicked = {
                                currentScreen = "login" // Keluar akun kembali ke halaman login
                            },
                            onUbahEmailClicked = {
                                currentScreen = "ubah_email" // <--- Ini yang membuat layarnya terbuka saat diklik
                            },
                            onGantiNoHpClicked = {
                                currentScreen = "ganti_nohp"
                            },
                            onGantiPasswordClicked = {
                                currentScreen = "ganti_password"
                            },
                            onVideoTutorialClicked = {
                                currentScreen = "video_tutorial"
                            },
                            onBantuanFaqClicked = {
                                currentScreen = "bantuan_faq"
                            },
                            onTentangAplikasiClicked = {
                                currentScreen = "tentang_aplikasi"
                            },
                            onBackClicked = {
                                currentScreen = "home"
                            }
                        )
                    }
                    "edit_profile" -> {
                        EditProfileScreen(
                            initialNama = "Sahal Alfarizi",
                            onBackClicked = { currentScreen = "profile" },
                            onSaveSuccess = { currentScreen = "profile" } // Kembali ke profil setelah klik simpan
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