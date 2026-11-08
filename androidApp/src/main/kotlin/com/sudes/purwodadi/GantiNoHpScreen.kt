package com.sudes.purwodadi

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sudes.purwodadi.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GantiNoHpScreen(
    onBackClicked: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    var noHpSaatIni by remember { mutableStateOf("081234567890") }
    var noHpBaru by remember { mutableStateOf("") }

    // State untuk pop-up OTP
    var showOtpDialog by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }

    // State untuk Timer Hitung Mundur (60 Detik)
    var timeLeft by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    // Efek hitung mundur otomatis saat pop-up OTP terbuka
    LaunchedEffect(showOtpDialog, timeLeft) {
        if (showOtpDialog && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft == 0) {
            canResend = true
        }
    }

    val scrollState = rememberScrollState()

    BackHandler {
        if (showOtpDialog) {
            // Jika pop-up OTP sedang terbuka, tutup pop-upnya dulu saat tombol Back ditekan
            showOtpDialog = false
        } else {
            // Jika tidak ada pop-up, kembali ke halaman sebelumnya (Profil)
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
                        text = "Ganti Nomor Handphone",
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
            // Field 1: Nomor Handphone Saat Ini (Disabled)
            NoHpFormTextField(
                label = "Nomor Handphone Saat Ini",
                value = noHpSaatIni,
                onValueChange = {},
                placeholder = "",
                enabled = false,
                keyboardType = KeyboardType.Phone
            )

            // Field 2: Nomor Handphone Baru
            NoHpFormTextField(
                label = "Nomor Handphone Baru",
                value = noHpBaru,
                onValueChange = { noHpBaru = it },
                placeholder = "Masukkan nomor WhatsApp/HP baru",
                enabled = true,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Kotak Catatan Verifikasi
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
                        text = "Catatan Verifikasi:",
                        style = AppTypography.LabelForm,
                        color = Color(0xFFB45309)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Kode verifikasi OTP akan dikirimkan ke nomor WhatsApp/HP baru Anda.",
                        style = AppTypography.BodyRegular,
                        color = Color(0xFF92400E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Kirim OTP
            Button(
                onClick = {
                    if (noHpBaru.isNotBlank()) {
                        timeLeft = 60 // Reset timer ke 60 detik
                        canResend = false
                        showOtpDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
            ) {
                Text(
                    text = "Kirim Kode OTP",
                    style = AppTypography.ButtonText
                )
            }
        }
    }

    // Pop-up Dialog Masukkan OTP & Timer 60 Detik
    if (showOtpDialog) {
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
                                contentDescription = "OTP",
                                tint = GreenDesaPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Verifikasi Kode OTP",
                        style = AppTypography.HeadingLarge,
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
                        text = "Masukkan 4 digit kode OTP yang dikirim ke:",
                        style = AppTypography.BodyRegular,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = noHpBaru.ifBlank { "0812XXXXXX" },
                        style = AppTypography.HeadingMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Kotak Input OTP
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 4) otpCode = it },
                        placeholder = { Text("0 0 0 0", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                        textStyle = AppTypography.HeadingLarge.copy(textAlign = TextAlign.Center, color = TextPrimary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(180.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = GreenDesaPrimary,
                            unfocusedBorderColor = GrayBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bagian Timer & Tombol Kirim Ulang
                    if (!canResend) {
                        Text(
                            text = "Kirim ulang kode dalam ${timeLeft} detik",
                            style = AppTypography.BodyRegular,
                            color = TextSecondary
                        )
                    } else {
                        TextButton(
                            onClick = {
                                timeLeft = 60
                                canResend = false
                            }
                        ) {
                            Text(
                                text = "Kirim Ulang OTP",
                                style = AppTypography.ButtonText.copy(color = GreenDesaPrimary)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (otpCode.length == 4) {
                            showOtpDialog = false
                            onSaveSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                ) {
                    Text(
                        text = "Konfirmasi & Simpan Nomor",
                        style = AppTypography.ButtonText
                    )
                }
            }
        )
    }
}

@Composable
fun NoHpFormTextField(
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