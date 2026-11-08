package com.sudes.purwodadi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

    // ==========================================
    // SAFETY NET: Mencegah Loading Stuck Saat Back Kilat
    // ==========================================
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var clickTime by remember { mutableStateOf(0L) }

    // Simpan waktu saat tombol Google diklik (Anda bisa panggil ini atau catat otomatis)
    // Atau kita deteksi berdasarkan berapa lama loading menyala:
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                // Jika aplikasi kembali aktif dan loading tersangkut LEBIH DARI 1.5 DETIK
                // (artinya benar-benar stuck akibat back kilat), baru matikan paksa.
                // Jika baru beberapa milidetik (proses normal Continue as), biarkan tetap loading!
                if (authViewModel.isLoading.value) {
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        if (authViewModel.isLoading.value) {
                            authViewModel.setLoading(false)
                        }
                    }, 1500) // Jeda 1.5 detik agar proses "Continue as" tidak terganggu
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
            .background(GrayBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ==========================================
            // HEADER (Bukan Card, Menempel di Atas Layar)
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GreenDesaPrimary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Panah Kembali ke Login
                    IconButton(
                        onClick = onBackToLoginClicked,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali ke Halaman Masuk",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "Buat Akun Baru",
                            style = AppTypography.HeadingLarge.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Silakan isi data diri Anda dengan benar ya",
                            style = AppTypography.BodyRegular.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                    }
                }
            }

            // ==========================================
            // BAGIAN FORM / DATA DIBUNGKUS DALAM CARD
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Tombol Cepat dengan Google
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
                                    text = "Daftar Cepat dengan Akun Google",
                                    style = AppTypography.LabelForm,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Pembatas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor, thickness = 1.dp)
                            Text(
                                text = "  ATAU ISI DATA DI BAWAH  ",
                                style = AppTypography.Caption.copy(fontWeight = FontWeight.Bold),
                                color = TextSecondary
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor, thickness = 1.dp)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        RegisterField(
                            label = "Nama Lengkap",
                            value = namaLengkap,
                            onValueChange = { namaLengkap = it },
                            placeholder = "Masukkan nama lengkap sesuai KTP"
                        )

                        RegisterField(
                            label = "Email",
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "Masukkan email Anda"
                        )

                        RegisterField(
                            label = "Nomor HP / WhatsApp",
                            value = nomorHp,
                            onValueChange = { nomorHp = it },
                            placeholder = "Contoh: 081234567890"
                        )

                        RegisterField(
                            label = "Kata Sandi",
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "Minimal 8 karakter",
                            isPassword = true,
                            passwordVisible = passwordVisible,
                            onVisibilityChanged = { passwordVisible = !passwordVisible }
                        )

                        RegisterField(
                            label = "Konfirmasi Kata Sandi",
                            value = konfirmasiPassword,
                            onValueChange = { konfirmasiPassword = it },
                            placeholder = "Ketik ulang kata sandi yang sama",
                            isPassword = true,
                            passwordVisible = confirmPasswordVisible,
                            onVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
                        )

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            // Kotak peringatan merah lembut agar sangat jelas terlihat
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFEF2F2), // Merah muda sangat lembut
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
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = errorMessage ?: "",
                                        color = MaterialTheme.colorScheme.error,
                                        style = AppTypography.BodyRegular.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
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
                                    text = "Daftar Akun Sekarang",
                                    style = AppTypography.ButtonText
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Tautan Kembali ke Login
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
        }

        // OVERLAY LOADING (Memblokir semua sentuhan/klik agar tidak tembus ke bawah)
        if (isAnyLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {
                        // Kotak ini sengaja dikosongkan agar sentuhan tertahan di sini
                        // dan tidak menembus ke tombol form di bawahnya
                    },
                contentAlignment = Alignment.Center
            ) {
                // Background kotak putih (Surface) telah dihapus di sini,
                // sehingga CircularProgressIndicator langsung tampil bersih di tengah layar
                CircularProgressIndicator(
                    color = GreenDesaPrimary,
                    modifier = Modifier.size(50.dp)
                )
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
                                    contentDescription = "Sukses",
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

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Yeay! Akun Anda sudah berhasil dibuat.\n\nKami telah mengirimkan pesan verifikasi ke alamat email Anda.\nSilakan buka kotak masuk (Inbox) atau folder Spam di email Anda, lalu klik tautan verifikasinya sebelum masuk kembali ke aplikasi ya.",
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
                                text = "Kembali ke Halaman Masuk",
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
                            contentDescription = "Tampilkan atau Sembunyikan Kata Sandi",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null
        )
    }
}