package com.sudes.purwodadi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
// TAHAP 3: HALAMAN PENDAFTARAN AKUN (REGISTER SCREEN)
// ==============================================================================
@Composable
fun RegisterScreen(
    onRegisterSubmitted: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onGoogleRegisterClicked: () -> Unit = {},
    onBackToLoginClicked: () -> Unit = {}
) {
    // State input form pendaftaran
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ----------------------------------------------------------------------
        // 1. HEADER ATAS (HIJAU DESA)
        // ----------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(GreenDesaPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🛡️", fontSize = 28.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SuDes Purwodadi",
                    style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 22.sp)
                )
            }
        }

        // ----------------------------------------------------------------------
        // 2. KARTU UTAMA FORM PENDAFTARAN
        // ----------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-24.dp))
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Daftar Akun",
                        style = AppTypography.HeadingLarge,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lengkapi data diri untuk membuat akun baru",
                        style = AppTypography.BodyRegular.copy(fontSize = 13.sp),
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Google Register
                    OutlinedButton(
                        onClick = onGoogleRegisterClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "🌐  Lanjut dengan Google", color = Color(0xFF334155))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Garis Pemisah ATAU
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                        Text(text = "  ATAU ISI DATA DIRI  ", style = AppTypography.Caption, color = Color.Gray)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nama Lengkap
                    Text(text = "Nama Lengkap", style = AppTypography.LabelForm, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = { Text("Masukkan nama lengkap Anda", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    Text(text = "Email", style = AppTypography.LabelForm, color = Color(0xFF1E293B))
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nomor HP / WhatsApp
                    Text(text = "Nomor HP", style = AppTypography.LabelForm, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("0812xxxxxxxx", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    Text(text = "Password", style = AppTypography.LabelForm, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Minimal 8 karakter", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Text(if (isPasswordVisible) "👁️" else "🙈")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Konfirmasi Password
                    Text(text = "Konfirmasi Password", style = AppTypography.LabelForm, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = { Text("Ulangi password Anda", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                                Text(if (isConfirmPasswordVisible) "👁️" else "🙈")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Tombol Daftar Sekarang
                    Button(
                        onClick = { onRegisterSubmitted(fullName, email, phone, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(text = "Daftar Sekarang", style = AppTypography.ButtonText)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tautan Kembali ke Login
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sudah punya akun? ",
                            style = AppTypography.BodyRegular.copy(fontSize = 13.sp),
                            color = Color.Gray
                        )
                        TextButton(
                            onClick = onBackToLoginClicked,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Masuk",
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