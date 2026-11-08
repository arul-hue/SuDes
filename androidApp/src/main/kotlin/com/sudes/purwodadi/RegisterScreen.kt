package com.sudes.purwodadi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = remember { AuthViewModel() },
    isLoading: Boolean = false,
    onGoogleRegisterClicked: () -> Unit = {},
    onBackToLoginClicked: () -> Unit = {}
) {
    val viewModelLoading by authViewModel.isLoading.collectAsState()
    val isAnyLoading = isLoading || viewModelLoading
    val errorMessage by authViewModel.errorMessage.collectAsState()

    var namaLengkap by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nomorHp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    BackHandler {
        onBackToLoginClicked()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        contentAlignment = Alignment.Center
    ) {
        // KARTU UTAMA PENDAFTARAN
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // Header Hijau di dalam kartu
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    color = GreenDesaPrimary
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Daftar Akun",
                            style = AppTypography.HeadingLarge.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Lengkapi data diri untuk membuat akun baru",
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                    }
                }

                // Bagian Isi Form
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Tombol Masuk dengan Google
                    OutlinedButton(
                        onClick = onGoogleRegisterClicked,
                        enabled = !isAnyLoading,
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pembatas "ATAU ISI DATA DIRI"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor, thickness = 1.dp)
                        Text(
                            text = "  ATAU ISI DATA DIRI  ",
                            style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondary
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor, thickness = 1.dp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField(
                        label = "Nama Lengkap",
                        value = namaLengkap,
                        onValueChange = { namaLengkap = it },
                        placeholder = "Masukkan nama lengkap Anda"
                    )

                    RegisterField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Masukkan email Anda"
                    )

                    RegisterField(
                        label = "Nomor HP",
                        value = nomorHp,
                        onValueChange = { nomorHp = it },
                        placeholder = "0812xxxxxxxx"
                    )

                    RegisterField(
                        label = "Password",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Minimal 8 karakter",
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onVisibilityChanged = { passwordVisible = !passwordVisible }
                    )

                    RegisterField(
                        label = "Konfirmasi Password",
                        value = konfirmasiPassword,
                        onValueChange = { konfirmasiPassword = it },
                        placeholder = "Ulangi password Anda",
                        isPassword = true,
                        passwordVisible = confirmPasswordVisible,
                        onVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = AppTypography.Caption
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol Daftar Sekarang
                    Button(
                        onClick = {
                            authViewModel.registerUser(
                                namaLengkap = namaLengkap,
                                email = email,
                                nomorHp = nomorHp,
                                password = password,
                                konfirmasiPassword = konfirmasiPassword,
                                onSuccess = {
                                    showSuccessDialog = true
                                }
                            )
                        },
                        enabled = !isAnyLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                    ) {
                        if (isAnyLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Daftar Sekarang",
                                style = AppTypography.ButtonText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sudah punya akun? ",
                            style = AppTypography.Caption,
                            color = TextSecondary
                        )
                        TextButton(
                            onClick = onBackToLoginClicked,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Masuk",
                                style = AppTypography.LabelForm.copy(fontWeight = FontWeight.Bold),
                                color = GreenDesaPrimary
                            )
                        }
                    }
                }
            }
        }

        // OVERLAY LOADING (Otomatis muncul saat isAnyLoading bernilai true)
        if (isAnyLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier.padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = GreenDesaPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }

        // POPUP SUKSES PENDAFTARAN
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
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Success",
                                    tint = GreenDesaPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Pendaftaran Berhasil!",
                            style = AppTypography.HeadingLarge,
                            color = GreenDesaPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Akun Anda berhasil dibuat & tersimpan di cloud.\nSilakan masuk untuk melanjutkan.",
                            style = AppTypography.BodyRegular,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

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
                                text = "Kembali ke Login",
                                style = AppTypography.ButtonText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityChanged: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = AppTypography.LabelForm,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
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
            ),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onVisibilityChanged) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null
        )
    }
}