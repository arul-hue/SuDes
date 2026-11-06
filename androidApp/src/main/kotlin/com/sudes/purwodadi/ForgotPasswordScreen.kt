package com.sudes.purwodadi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudes.purwodadi.ui.theme.*

@Composable
fun ForgotPasswordScreen(
    onSubmitEmailClicked: (String) -> Unit = {},
    onBackToLoginClicked: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        contentAlignment = Alignment.Center
    ) {
        // KARTU UTAMA DENGAN HEADER HIJAU DI DALAMNYA (UI TETAP PERSIS)
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
                // Header Hijau di bagian atas kartu
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
                            style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Masukkan email terdaftar untuk reset password.",
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
                        )
                    }
                }

                // Bagian Form Input di dalam kartu dengan font besar
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Email",
                        style = AppTypography.LabelForm.copy(fontSize = 15.sp),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                "Masukkan email Anda",
                                color = TextPlaceholder,
                                style = AppTypography.BodyRegular.copy(fontSize = 15.sp)
                            )
                        },
                        textStyle = AppTypography.BodyRegular.copy(fontSize = 15.sp, color = TextPrimary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                showSuccessDialog = true
                                onSubmitEmailClicked(email)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(
                            text = "Kirim Link Reset Password",
                            style = AppTypography.ButtonText.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = onBackToLoginClicked) {
                            Text(
                                text = "Kembali ke Halaman Masuk",
                                color = GreenDesaPrimary,
                                style = AppTypography.ButtonText.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // POPUP SUKSES KETIKA EMAIL DIKIRIM (FONT DIPERBESAR)
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { },
                containerColor = Color(0xFFF0FDF4),
                shape = RoundedCornerShape(24.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Periksa Email Anda",
                            style = AppTypography.HeadingLarge.copy(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                            color = GreenDesaPrimary
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tautan reset password telah dikirim ke",
                            style = AppTypography.BodyRegular.copy(fontSize = 15.sp),
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = email.ifBlank { "sahal@gmail.com" },
                            style = AppTypography.HeadingMedium.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Silakan cek kotak masuk atau folder spam Anda.",
                            style = AppTypography.BodyRegular.copy(fontSize = 14.sp),
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
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
                            style = AppTypography.ButtonText.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            )
        }
    }
}