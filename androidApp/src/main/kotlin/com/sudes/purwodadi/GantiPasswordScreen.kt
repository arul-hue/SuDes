package com.sudes.purwodadi

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.sudes.purwodadi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GantiPasswordScreen(
    onBackClicked: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val context = LocalContext.current

    var passwordSaatIni by remember { mutableStateOf("") }
    var passwordBaru by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSavedSuccess by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    BackHandler {
        if (isSavedSuccess) {
            isSavedSuccess = false
            onSaveSuccess()
        } else {
            onBackClicked()
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GreenDesaPrimary,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ganti Password",
                        style = AppTypography.HeadingMedium.copy(color = Color.White)
                    )
                }
            }
        },
        containerColor = GrayBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Field 1: Password Saat Ini
            PasswordFormTextField(
                label = "Password Saat Ini",
                value = passwordSaatIni,
                onValueChange = { passwordSaatIni = it },
                placeholder = "Masukkan password lama"
            )

            // Field 2: Password Baru
            PasswordFormTextField(
                label = "Password Baru",
                value = passwordBaru,
                onValueChange = { passwordBaru = it },
                placeholder = "Minimal 6 karakter"
            )

            // Field 3: Konfirmasi Password Baru
            PasswordFormTextField(
                label = "Konfirmasi Password Baru",
                value = konfirmasiPassword,
                onValueChange = { konfirmasiPassword = it },
                placeholder = "Masukkan ulang password baru"
            )

            // Pesan Error jika ada kendala validasi / firebase
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFEF2F2),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Peringatan",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = AppTypography.BodyRegular.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Kotak Tips Keamanan
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE047))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "Tips Keamanan:",
                        style = AppTypography.LabelForm,
                        color = Color(0xFFB45309)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Gunakan kombinasi huruf, angka, dan simbol untuk keamanan akun.",
                        style = AppTypography.BodyRegular,
                        color = Color(0xFF92400E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Simpan Password Baru dengan Integrasi Firebase Auth
            Button(
                onClick = {
                    errorMessage = null

                    if (passwordSaatIni.isBlank() || passwordBaru.isBlank() || konfirmasiPassword.isBlank()) {
                        errorMessage = "Semua kolom wajib diisi!"
                        return@Button
                    }

                    if (passwordBaru.length < 6) {
                        errorMessage = "Password baru minimal harus 6 karakter!"
                        return@Button
                    }

                    if (passwordBaru != konfirmasiPassword) {
                        errorMessage = "Konfirmasi password baru tidak cocok!"
                        return@Button
                    }

                    val email = currentUser?.email
                    if (email == null) {
                        errorMessage = "Sesi pengguna tidak valid. Silakan masuk ulang."
                        return@Button
                    }

                    isLoading = true
                    val credential = EmailAuthProvider.getCredential(email, passwordSaatIni)

                    // 1. Re-autentikasi user dengan password lama untuk verifikasi keamanan
                    currentUser.reauthenticate(credential)
                        .addOnSuccessListener {
                            // 2. Jika password lama benar, update ke password baru
                            currentUser.updatePassword(passwordBaru)
                                .addOnSuccessListener {
                                    isLoading = false
                                    isSavedSuccess = true
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    errorMessage = "Gagal memperbarui password: ${e.localizedMessage}"
                                }
                        }
                        .addOnFailureListener {
                            isLoading = false
                            errorMessage = "Password saat ini yang Anda masukkan salah!"
                        }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
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
                        text = "Simpan Password Baru",
                        style = AppTypography.ButtonText
                    )
                }
            }
        }
    }

    // Pop-up Dialog Sukses
    if (isSavedSuccess) {
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
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Berhasil Disimpan!",
                        style = AppTypography.HeadingLarge,
                        color = GreenDesaPrimary
                    )
                }
            },
            text = {
                Text(
                    text = "Password Anda telah berhasil diperbarui.",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        isSavedSuccess = false
                        onSaveSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(
                        text = "Kembali ke Profil",
                        style = AppTypography.ButtonText
                    )
                }
            }
        )
    }
}

@Composable
fun PasswordFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(bottom = 14.dp)) {
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
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description, tint = TextSecondary, modifier = Modifier.size(20.dp))
                }
            },
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
    }
}