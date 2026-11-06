package com.sudes.purwodadi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // State untuk mengatur halaman aktif (default "login")
            var currentScreen by remember { mutableStateOf("login") }

            when (currentScreen) {
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
                            // Setelah daftar sukses, kembali ke login
                            currentScreen = "login"
                        },
                        onGoogleRegisterClicked = {
                            // Aksi daftar Google
                        },
                        onBackToLoginClicked = {
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
                            // Aksi ketika salah satu surat diklik
                        },
                        onTabSelected = { tabIndex ->
                            // Aksi navigasi bawah (Beranda, Status, Profil)
                        }
                    )
                }
            }
        }
    }
}