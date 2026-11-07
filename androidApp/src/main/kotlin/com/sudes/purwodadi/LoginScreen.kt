package com.sudes.purwodadi

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.sudes.purwodadi.ui.theme.*

@Composable
fun LoginScreen(
    onLoginClicked: (String, String) -> Unit = { _, _ -> },
    onGoogleLoginClicked: () -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onRegisterClicked: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER ATAS HIJAU DESA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(GreenDesaPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // LOGO SUDES
                Surface(
                    modifier = Modifier.size(90.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White,
                    shadowElevation = 6.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_sudes),
                            contentDescription = "Logo SuDes",
                            modifier = Modifier.size(140.dp)
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
                    style = AppTypography.Caption.copy(color = Color.White.copy(alpha = 0.9f))
                )
            }
        }

        // KARTU UTAMA FORM LOGIN
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-32.dp))
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
                        text = "Masuk Akun",
                        style = AppTypography.HeadingLarge,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Silakan masuk menggunakan email terdaftar",
                        style = AppTypography.Caption,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Email", style = AppTypography.LabelForm, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Masukkan email Anda", color = TextPlaceholder, style = AppTypography.BodyRegular) },
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

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Password", style = AppTypography.LabelForm, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Masukkan password Anda", color = TextPlaceholder, style = AppTypography.BodyRegular) },
                        textStyle = AppTypography.BodyRegular.copy(color = TextPrimary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            unfocusedBorderColor = GrayBorder,
                            focusedBorderColor = GreenDesaPrimary,
                            focusedLabelColor = GreenDesaPrimary,
                            cursorColor = GreenDesaPrimary
                        ),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(
                            onClick = onForgotPasswordClicked,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Lupa Password?",
                                style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                                color = GreenDesaPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onLoginClicked(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        Text(text = "Masuk", style = AppTypography.ButtonText)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
                        Text(text = "  ATAU  ", style = AppTypography.Caption, color = TextSecondary)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Masuk dengan Google
                    OutlinedButton(
                        onClick = onGoogleLoginClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(BorderColor)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Logo Google",
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Masuk dengan Google",
                                style = AppTypography.LabelForm,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // DIPERBAIKI: Menggunakan Arrangement.Center pada Row agar teks berada di tengah secara horizontal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Belum punya akun? ",
                            style = AppTypography.Caption,
                            color = TextSecondary
                        )
                        TextButton(
                            onClick = onRegisterClicked,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Daftar",
                                style = AppTypography.LabelForm.copy(fontWeight = FontWeight.Bold),
                                color = GreenDesaPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}