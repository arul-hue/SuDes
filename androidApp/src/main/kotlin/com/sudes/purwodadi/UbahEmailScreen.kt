package com.sudes.purwodadi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbahEmailScreen(
    onBackClicked: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    var emailSaatIni by remember { mutableStateOf("sahal@gmail.com") }
    var emailBaru by remember { mutableStateOf("") }
    var passwordKonfirmasi by remember { mutableStateOf("") }
    var isSavedSuccess by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
                        text = "Ubah Email Google",
                        style = AppTypography.HeadingLarge.copy(color = Color.White, fontSize = 20.sp)
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
            // Field 1: Email Saat Ini (Disabled)
            UbahEmailFormTextField(
                label = "Email Google Saat Ini",
                value = emailSaatIni,
                onValueChange = {},
                placeholder = "",
                enabled = false
            )

            // Field 2: Email Baru
            UbahEmailFormTextField(
                label = "Email Google Baru",
                value = emailBaru,
                onValueChange = { emailBaru = it },
                placeholder = "Masukkan email baru yang aktif",
                enabled = true
            )

            // Field 3: Password Akun
            UbahEmailFormTextField(
                label = "Password Akun SuDes",
                value = passwordKonfirmasi,
                onValueChange = { passwordKonfirmasi = it },
                placeholder = "Masukkan password untuk konfirmasi",
                enabled = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Kotak Catatan Keamanan
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
                        text = "Catatan Keamanan:",
                        style = AppTypography.BodyRegular.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Anda akan diminta verifikasi ulang melalui email baru setelah disimpan.",
                        style = AppTypography.BodyRegular.copy(
                            fontSize = 13.sp,
                            color = Color(0xFF92400E)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Simpan Perubahan Email
            Button(
                onClick = {
                    if (emailBaru.isNotBlank() && passwordKonfirmasi.isNotBlank()) {
                        isSavedSuccess = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
            ) {
                Text(
                    text = "Simpan Perubahan Email",
                    style = AppTypography.ButtonText.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }

    // Pop-up Dialog Sukses Pengiriman Verifikasi Email (Gaya seperti ForgotPassword)
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
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Sent",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Periksa Email Baru Anda",
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
                        text = "Tautan verifikasi telah dikirimkan ke",
                        style = AppTypography.BodyRegular.copy(fontSize = 15.sp),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = emailBaru.ifBlank { "emailbaru@gmail.com" },
                        style = AppTypography.HeadingMedium.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Silakan cek kotak masuk atau folder spam untuk mengonfirmasi perubahan email.",
                        style = AppTypography.BodyRegular.copy(fontSize = 14.sp),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
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
                        style = AppTypography.ButtonText.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        )
    }
}

@Composable
fun UbahEmailFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean
) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(
            text = label,
            style = AppTypography.LabelForm.copy(fontSize = 15.sp),
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
                    style = AppTypography.BodyRegular.copy(fontSize = 15.sp)
                )
            },
            textStyle = AppTypography.BodyRegular.copy(fontSize = 15.sp, color = TextPrimary),
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