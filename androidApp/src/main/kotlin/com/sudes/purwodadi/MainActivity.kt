package com.sudes.purwodadi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.sudes.purwodadi.ui.theme.SudesTheme // Pastikan import theme sudah ada

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Bungkus seluruh navigasi aplikasi dengan SudesTheme agar tema hijau aktif secara global
            SudesTheme {
                // State untuk mengatur halaman aktif (dimulai dari "splash")
                var currentScreen by remember { mutableStateOf("splash") }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onTimeout = {
                                // Setelah animasi splash selesai, pindah ke halaman login
                                currentScreen = "login"
                            }
                        )
                    }
                    "login" -> {
                        LoginScreen(
                            onLoginClicked = { email, password ->
                                // Jika login berhasil, pindah ke halaman "home" (Beranda)
                                currentScreen = "home"
                            },
                            onGoogleLoginClicked = {
                                // Aksi Google Login
                            },
                            onForgotPasswordClicked = {
                                currentScreen = "forgot_password"
                            },
                            onRegisterClicked = {
                                currentScreen = "register"
                            }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            onRegisterSubmitted = { name, email, phone, password ->
                                // Kosongkan bagian ini agar halaman tidak langsung tertutup.
                                // Biarkan pop-up sukses tampil di RegisterScreen.
                            },
                            onGoogleRegisterClicked = {
                                // Aksi daftar Google
                            },
                            onBackToLoginClicked = {
                                // Ini akan dipanggil saat tombol "Kembali ke Login" di DALAM POP-up diklik
                                currentScreen = "login"
                            }
                        )
                    }
                    "forgot_password" -> {
                        ForgotPasswordScreen(
                            onSubmitEmailClicked = { email ->
                                // Aksi kirim email reset password
                            },
                            onBackToLoginClicked = {
                                currentScreen = "login"
                            }
                        )
                    }
                    "home" -> {
                        HomeScreen(
                            userName = "Sahal",
                            onServiceSelected = { serviceName ->
                                // Jika Surat Keterangan Usaha diklik, buka SuratUsahaScreen
                                if (serviceName == "Surat Keterangan Usaha") {
                                    currentScreen = "surat_usaha"
                                } else if (serviceName == "Surat Keterangan Domisili") {
                                    currentScreen = "surat_domisili" // <- Menuju ke Surat Domisili
                                } else if (serviceName == "Surat Keterangan Kehilangan") {
                                    currentScreen = "surat_kehilangan" // <- Menuju ke Surat Kehilangan
                                } else if (serviceName == "Surat Keterangan Kematian") {
                                    currentScreen = "surat_kematian" // <- Menuju ke Surat Kematian
                                } else if (serviceName == "Surat Keterangan Tidak Mampu") {
                                    currentScreen = "surat_sktm" // <- Menuju ke Surat Tidak Mampu
                                }
                            },
                            onTabSelected = { tabIndex ->
                                // Aksi navigasi bawah (Beranda, Status, Profil)
                            }
                        )
                    }
                    "surat_usaha" -> {
                        SuratUsahaScreen(
                            onBackClicked = {
                                // Kembali ke beranda
                                currentScreen = "home"
                            },
                            onSubmitSuccess = {
                                // Aksi tambahan setelah berhasil submit jika diperlukan
                            }
                        )
                    }
                    "surat_domisili" -> {
                        SuratDomisiliScreen(
                            onBackClicked = {
                                // Kembali ke beranda
                                currentScreen = "home"
                            },
                            onSubmitSuccess = {
                                // Aksi tambahan setelah berhasil submit jika diperlukan
                            }
                        )
                    }
                    "surat_kehilangan" -> {
                        SuratKehilanganScreen(
                            onBackClicked = {
                                // Kembali ke beranda
                                currentScreen = "home"
                            },
                            onSubmitSuccess = {
                                // Aksi tambahan setelah berhasil submit jika diperlukan
                            }
                        )
                    }
                    "surat_kematian" -> {
                        SuratKematianScreen(
                            onBackClicked = {
                                // Kembali ke beranda
                                currentScreen = "home"
                            },
                            onSubmitSuccess = {
                                // Aksi tambahan setelah berhasil submit jika diperlukan
                            }
                        )
                    }
                    "surat_sktm" -> {
                        SuratTidakMampuScreen(
                            onBackClicked = {
                                // Kembali ke beranda
                                currentScreen = "home"
                            },
                            onSubmitSuccess = {
                                // Aksi tambahan setelah berhasil submit jika diperlukan
                            }
                        )
                    }
                }
            }
        }
    }
}