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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sudes.purwodadi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GantiEmailScreen(
    onBackClicked: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val context = LocalContext.current

    var emailSaatIni by remember { mutableStateOf("email@gmail.com") }
    var emailBaru by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        currentUser?.reload()?.addOnCompleteListener {
            emailSaatIni = currentUser.email ?: "email@gmail.com"
        }
    }

    val scrollState = rememberScrollState()

    BackHandler {
        if (showSuccessDialog) {
            showSuccessDialog = false
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
                        text = "Ganti Email Google",
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
            // CATATAN SINGKAT & PADAT DI ATAS
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ℹ️ Info Penting:",
                        style = AppTypography.LabelForm.copy(color = Color(0xFF1E40AF)),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Fitur ini paling pas untuk akun **Manual (Email & Password)**. Jika Anda pakai Akun Google, email utama mengikuti pengaturan HP Anda.",
                        style = AppTypography.BodyRegular.copy(color = Color(0xFF1E3A8A))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GantiEmailFormTextField(
                label = "Email Saat Ini",
                value = emailSaatIni,
                onValueChange = {},
                placeholder = "",
                enabled = false,
                keyboardType = KeyboardType.Email
            )

            GantiEmailFormTextField(
                label = "Email Baru",
                value = emailBaru,
                onValueChange = { emailBaru = it },
                placeholder = "Ketik email baru yang aktif",
                enabled = true,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(4.dp))

            // PANDUAN RINGKAS DI BAWAH
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE047))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "💡 Cara Ganti:",
                        style = AppTypography.HeadingMedium.copy(color = Color(0xFFB45309))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1. Masukkan email baru.\n2. Klik tombol kirim, lalu buka email baru Anda.\n3. Klik tautan verifikasi yang masuk untuk mengaktifkan.",
                        style = AppTypography.BodyRegular.copy(color = Color(0xFF92400E))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (emailBaru.isBlank()) {
                        Toast.makeText(context, "Email baru tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    currentUser?.verifyBeforeUpdateEmail(emailBaru)
                        ?.addOnSuccessListener {
                            val userId = currentUser.uid
                            FirebaseFirestore.getInstance().collection("users").document(userId)
                                .update("email", emailBaru)
                                .addOnSuccessListener {
                                    isLoading = false
                                    showSuccessDialog = true
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    Toast.makeText(context, "Gagal memperbarui database: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        ?.addOnFailureListener { e ->
                            isLoading = false
                            Toast.makeText(context, "Gagal mengubah email: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
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
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Kirim Tautan Verifikasi",
                        style = AppTypography.ButtonText
                    )
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        color = GreenDesaLight
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Terkirim",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tautan Terkirim!",
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    text = "Silakan cek kotak masuk email baru Anda ($emailBaru) dan klik tautan verifikasi yang dikirim untuk menyelesaikan perubahan.",
                    style = AppTypography.BodyRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onSaveSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(
                        text = "Baik, Mengerti",
                        style = AppTypography.ButtonText
                    )
                }
            }
        )
    }
}

@Composable
fun GantiEmailFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean,
    keyboardType: KeyboardType
) {
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
            enabled = enabled,
            placeholder = {
                Text(
                    placeholder,
                    color = TextPlaceholder,
                    style = AppTypography.BodyRegular
                )
            },
            textStyle = AppTypography.BodyRegular.copy(color = TextPrimary),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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