package com.sudes.purwodadi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    isLoading: Boolean,
    onBackToLoginClicked: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Mengambil state error secara real-time dari ViewModel
    val errorMessage by authViewModel.errorMessage.collectAsState()

    // Menambahkan BackHandler
    BackHandler {
        if (showSuccessDialog) {
            showSuccessDialog = false
        } else {
            authViewModel.clearError()
            onBackToLoginClicked()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header Hijau
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    color = GreenDesaPrimary
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Atur Ulang Password",
                            style = AppTypography.HeadingLarge.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Masukkan email terdaftar untuk reset password.",
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                    }
                }

                // Bagian Form Input
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Tampilkan kotak error jika ada pesan error dari ViewModel
                    if (!errorMessage.isNullOrEmpty()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEE2E2)
                        ) {
                            Text(
                                text = errorMessage ?: "",
                                color = Color(0xFF991B1B),
                                style = AppTypography.BodyRegular,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Text(
                        text = "Email",
                        style = AppTypography.LabelForm,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (errorMessage != null) authViewModel.clearError()
                        },
                        placeholder = {
                            Text(
                                "Masukkan email Anda",
                                color = TextPlaceholder,
                                style = AppTypography.BodyRegular
                            )
                        },
                        textStyle = AppTypography.BodyRegular.copy(color = TextPrimary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            unfocusedBorderColor = GrayBorder,
                            focusedBorderColor = GreenDesaPrimary,
                            focusedLabelColor = GreenDesaPrimary,
                            cursorColor = GreenDesaPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            authViewModel.sendPasswordResetEmail(email) {
                                // Dipanggil hanya jika Firebase sukses mengirim email
                                showSuccessDialog = true
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Kirim Link Reset Password",
                                style = AppTypography.ButtonText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    authViewModel.clearError()
                                    onBackToLoginClicked()
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Kembali ke Halaman Masuk",
                                color = GreenDesaPrimary,
                                style = AppTypography.ButtonText
                            )
                        }
                    }
                }
            }
        }

        // POPUP SUKSES
        if (showSuccessDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFF0FDF4),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(68.dp),
                            shape = CircleShape,
                            color = GreenDesaLight
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email Sent",
                                    tint = GreenDesaPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Periksa Email Anda",
                            style = AppTypography.HeadingLarge,
                            color = GreenDesaPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Tautan reset password telah dikirim ke",
                            style = AppTypography.BodyRegular,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = email.ifBlank { "email@desa.id" },
                            style = AppTypography.HeadingMedium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Silakan cek kotak masuk atau folder spam Anda.",
                            style = AppTypography.Caption,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                showSuccessDialog = false
                                authViewModel.clearError()
                                onBackToLoginClicked()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                        ) {
                            Text(
                                text = "Kembali ke Halaman Masuk",
                                style = AppTypography.ButtonText
                            )
                        }
                    }
                }
            }
        }
    }
}