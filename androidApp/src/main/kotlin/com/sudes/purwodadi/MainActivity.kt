package com.sudes.purwodadi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.sudes.purwodadi.ui.theme.AppTypography // Menjaga konsistensi tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // State untuk mengatur halaman mana yang sedang aktif
            // Nilai awal: "login"
            var currentScreen by remember { mutableStateOf("login") }

            // Logika perpindahan antar layar (Routing sederhana)
            when (currentScreen) {
                "login" -> {
                    LoginScreen(
                        onLoginClicked = { email, password ->
                            // Aksi ketika tombol Masuk ditekan
                            // (Nanti bisa dihubungkan ke backend/Firebase)
                        },
                        onGoogleLoginClicked = {
                            // Aksi login Google
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
                            // Aksi pendaftaran berhasil, kembali ke login
                            currentScreen = "login"
                        },
                        onGoogleRegisterClicked = {
                            // Aksi daftar dengan Google
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
            }
        }
    }
}