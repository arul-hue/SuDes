package com.sudes.purwodadi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudes.purwodadi.ui.theme.AppTypography
import com.sudes.purwodadi.ui.theme.GrayBackground
import com.sudes.purwodadi.ui.theme.GreenDesaPrimary

@Composable
fun ForgotPasswordScreen(
    onSubmitEmailClicked: (String) -> Unit = {},
    onBackToLoginClicked: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!isSubmitted) {
                        // Header Hijau di dalam Kartu
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = GreenDesaPrimary,
                                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Atur Ulang Password",
                                    style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 22.sp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Masukkan email terdaftar untuk reset password.",
                                    style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Email",
                                style = AppTypography.LabelForm,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = { Text("Masukkan email Anda", color = Color.LightGray) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (email.isNotBlank()) {
                                        isSubmitted = true
                                        onSubmitEmailClicked(email)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                            ) {
                                Text(text = "Kirim Link Reset Password", style = AppTypography.ButtonText)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = onBackToLoginClicked) {
                                    Text(
                                        text = "Kembali ke Halaman Masuk",
                                        style = AppTypography.BodyRegular.copy(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = GreenDesaPrimary
                                    )
                                }
                            }
                        }
                    } else {
                        // Tampilan Sukses Terkirim
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                modifier = Modifier.size(64.dp),
                                shape = RoundedCornerShape(32.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "✉️", fontSize = 28.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Periksa Email Anda",
                                style = AppTypography.HeadingLarge,
                                color = Color(0xFF1E293B)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Tautan reset password telah dikirim ke\n$email",
                                style = AppTypography.BodyRegular.copy(fontSize = 13.sp),
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = onBackToLoginClicked,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                            ) {
                                Text(text = "Kembali ke Halaman Masuk", style = AppTypography.ButtonText)
                            }
                        }
                    }
                }
            }
        }
    }
}