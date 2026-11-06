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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudes.purwodadi.ui.theme.AppTypography
import com.sudes.purwodadi.ui.theme.GrayBackground
import com.sudes.purwodadi.ui.theme.GreenDesaPrimary

// ==============================================================================
// TAHAP 2: HALAMAN MASUK AKUN (SESUAI DESAIN FIGMA)
// ==============================================================================
@Composable
fun LoginScreen(
    onLoginClicked: (String, String) -> Unit = { _, _ -> },
    onGoogleLoginClicked: () -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onRegisterClicked: () -> Unit = {}
) {
    // State input form
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ----------------------------------------------------------------------
        // 1. HEADER ATAS (WARNA HIJAU DESA + LOGO SUDES)
        // ----------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.38f)
                .background(GreenDesaPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Shield Icon Placeholder / Container Logo White
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Teks darurat pengganti logo tameng (Shield)
                        Text(
                            text = "🛡️",
                            fontSize = 36.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "SuDes",
                    style = AppTypography.HeadingLarge.copy(color = Color.White)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Layanan Surat Online Desa",
                    style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f))
                )
            }
        }

        // ----------------------------------------------------------------------
        // 2. KARTU PUTIH FORM LOGIN (OVERLAP / FLOATING CARD)
        // ----------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.62f)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp), // Efek menumpuk ke atas header hijau
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Masuk Akun",
                        style = AppTypography.HeadingLarge,
                        color = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Silakan masuk menggunakan email terdaftar",
                        style = AppTypography.BodyRegular.copy(fontSize = 13.sp),
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Input Field Email
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
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Input Field Password
                    Text(
                        text = "Password",
                        style = AppTypography.LabelForm,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Masukkan password Anda", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Text(if (isPasswordVisible) "👁️" else "🙈")
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedContainerColor = Color.White
                        )
                    )

                    // Link Lupa Password
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(onClick = onForgotPasswordClicked) {
                            Text(
                                text = "Lupa Password?",
                                style = AppTypography.LabelForm.copy(fontWeight = FontWeight.Bold),
                                color = GreenDesaPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tombol Masuk Utama
                    Button(
                        onClick = { onLoginClicked(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(
                            text = "Masuk",
                            style = AppTypography.ButtonText
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pemisah ATAU
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                        Text(
                            text = "  ATAU  ",
                            style = AppTypography.Caption,
                            color = Color.Gray
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Lanjut dengan Google
                    OutlinedButton(
                        onClick = onGoogleLoginClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "🌐  Lanjut dengan Google", color = Color(0xFF334155))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Link Daftar Akun
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Belum punya akun? ",
                            style = AppTypography.BodyRegular.copy(fontSize = 13.sp),
                            color = Color.Gray
                        )
                        TextButton(
                            onClick = onRegisterClicked,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Daftar",
                                style = AppTypography.BodyRegular.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = GreenDesaPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}