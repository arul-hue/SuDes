package com.sudes.purwodadi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import com.sudes.purwodadi.ui.theme.SudesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudesTheme {
                var currentScreen by remember { mutableStateOf("splash") }

                // Inisialisasi AuthViewModel utama di MainActivity
                val authViewModel = remember { AuthViewModel() }
                val isLoading by authViewModel.isLoading.collectAsState()

                // RIWAYAT TAB GLOBAL (Menyimpan urutan tab: "home", "status", "profile")
                val tabHistory = remember { mutableStateListOf("home") }
                var backPressedTime by remember { mutableStateOf(0L) }

                // State untuk menyimpan data surat yang sedang dipilih ke halaman detail
                var selectedJenisSurat by remember { mutableStateOf("Surat Keterangan Domisili") }
                var selectedStatusSurat by remember { mutableStateOf(StatusPengajuan.PROSES) }
                var selectedAlasanDitolak by remember { mutableStateOf<String?>(null) }
                var selectedNamaFileRt by remember { mutableStateOf("pengantar_rt03_sahal.jpg") }

                var selectedFields by remember {
                    mutableStateOf(
                        listOf(
                            DetailFieldItem("Nama Lengkap", "Sahal Aw"),
                            DetailFieldItem("NIK", "3501230101900001"),
                            DetailFieldItem("Tempat, Tanggal Lahir", "Bandar Lampung, 01 Januari 1990"),
                            DetailFieldItem("Jenis Kelamin", "Laki-laki"),
                            DetailFieldItem("Agama", "Islam"),
                            DetailFieldItem("Pekerjaan", "Wiraswasta"),
                            DetailFieldItem("Alamat Lengkap", "Jl. Melati No. 12, RT 03/RW 05, Desa Purwodadi Simpang"),
                            DetailFieldItem("Alamat Tempat Tinggal Saat Ini", "Dusun II RT 03, Purwodadi Simpang, Kec. Tanjung Bintang")
                        )
                    )
                }

                val context = this

                if (currentScreen == "home" || currentScreen == "status" || currentScreen == "profile") {
                    BackHandler {
                        if (tabHistory.size > 1) {
                            // Diperbaiki: Typo patahan baris newline ( \n ) dibersihkan menjadi minus (-)
                            tabHistory.removeAt(tabHistory.size - 1)
                            currentScreen = tabHistory.last()
                        } else {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - backPressedTime < 2000) {
                                finish()
                            } else {
                                backPressedTime = currentTime
                                Toast.makeText(context, "Tekan sekali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onTimeout = { currentScreen = "login" }
                        )
                    }
                    "login" -> {
                        LoginScreen(
                            onLoginClicked = { email, _ ->
                                if (email.equals("admin@purwodadi.desa.id", ignoreCase = true) ||
                                    email.contains("admin", ignoreCase = true)) {
                                    currentScreen = "admin_dashboard"
                                } else {
                                    currentScreen = "home"
                                }
                                tabHistory.clear()
                                tabHistory.add(currentScreen)
                            },
                            onGoogleLoginClicked = {
                                currentScreen = "home"
                                tabHistory.clear()
                                tabHistory.add("home")
                            },
                            onForgotPasswordClicked = { currentScreen = "forgot_password" },
                            onRegisterClicked = { currentScreen = "register" }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            authViewModel = authViewModel,
                            isLoading = isLoading,
                            onGoogleRegisterClicked = {
                                // CEK INSTAN: Jika sudah dalam keadaan loading, abaikan ketukan tambahan
                                if (authViewModel.isLoading.value) return@RegisterScreen

                                // Nyalakan loading seketika saat tombol Google diklik
                                authViewModel.setLoading(true)

                                val credentialManager = CredentialManager.create(this@MainActivity)

                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId("623723710056-fup3cgmup9pqfmvhikba14clm1vrqrl9.apps.googleusercontent.com")
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                lifecycleScope.launch {
                                    try {
                                        val result = credentialManager.getCredential(this@MainActivity, request)
                                        val credential = result.credential

                                        if (credential is CustomCredential &&
                                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                            val googleIdToken = googleIdTokenCredential.idToken
                                            val googleName = googleIdTokenCredential.displayName ?: "Warga Purwodadi"
                                            val googleEmail = googleIdTokenCredential.id

                                            // Proses login ke Firebase (Loading TETAP AKTIF selama proses ini)
                                            authViewModel.signInWithGoogleCredential(
                                                idToken = googleIdToken,
                                                namaLengkap = googleName,
                                                email = googleEmail,
                                                onSuccess = {
                                                    // Matikan loading HANYA KETIKA semuanya benar-benar sukses & pindah halaman
                                                    authViewModel.setLoading(false)
                                                    currentScreen = "home"
                                                    tabHistory.clear()
                                                    tabHistory.add("home")
                                                    Toast.makeText(this@MainActivity, "Berhasil masuk dengan Google!", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        } else {
                                            // Jika format kredensial tidak sesuai
                                            authViewModel.setLoading(false)
                                        }
                                    } catch (e: Throwable) {
                                        val errorName = e.javaClass.name.lowercase()
                                        val isCanceled = errorName.contains("cancel") ||
                                                errorName.contains("abort") ||
                                                e is kotlinx.coroutines.CancellationException

                                        if (!isCanceled) {
                                            Toast.makeText(this@MainActivity, "Google Sign-In gagal: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                        }
                                        // Matikan loading jika terjadi pembatalan atau error
                                        authViewModel.setLoading(false)
                                    }
                                    // Blok `finally` dihapus dari sini agar loading tidak mati terlalu cepat!
                                }
                            },
                            onBackToLoginClicked = { currentScreen = "login" }
                        )
                    }
                    "forgot_password" -> {
                        ForgotPasswordScreen(
                            onSubmitEmailClicked = { _ -> currentScreen = "login" },
                            onBackToLoginClicked = { currentScreen = "login" }
                        )
                    }
                    "admin_dashboard" -> {
                        AdminDashboardScreen(
                            onDetailClicked = { _ -> },
                            onSelesaikanClicked = { currentScreen = "admin_edit_pengajuan" },
                            onLogoutClicked = {
                                currentScreen = "login"
                                tabHistory.clear()
                            }
                        )
                    }
                    "admin_edit_pengajuan" -> {
                        AdminEditPengajuanScreen(
                            onBackClicked = { currentScreen = "admin_dashboard" },
                            onSaveSuccess = { currentScreen = "admin_dashboard" }
                        )
                    }
                    "home" -> {
                        HomeScreen(
                            userName = "Sahal",
                            onServiceSelected = { serviceName ->
                                when (serviceName) {
                                    "Surat Keterangan Usaha" -> currentScreen = "surat_usaha"
                                    "Surat Keterangan Domisili" -> currentScreen = "surat_domisili"
                                    "Surat Keterangan Kehilangan" -> currentScreen = "surat_kehilangan"
                                    "Surat Keterangan Kematian" -> currentScreen = "surat_kematian"
                                    "Surat Keterangan Tidak Mampu" -> currentScreen = "surat_sktm"
                                }
                            },
                            onTabSelected = { tabIndex ->
                                val target = when (tabIndex) {
                                    1 -> "status"
                                    2 -> "profile"
                                    else -> "home"
                                }
                                if (currentScreen != target) {
                                    currentScreen = target
                                    tabHistory.add(target)
                                }
                            }
                        )
                    }
                    "status" -> {
                        StatusScreen(
                            onTabSelected = { tabIndex ->
                                val target = when (tabIndex) {
                                    0 -> "home"
                                    2 -> "profile"
                                    else -> "status"
                                }
                                if (currentScreen != target) {
                                    currentScreen = target
                                    tabHistory.add(target)
                                }
                            },
                            onDetailClicked = { jenisSurat ->
                                selectedJenisSurat = jenisSurat
                                currentScreen = "detail_pengajuan"
                            },
                            onResubmitClicked = { jenisSurat ->
                                when {
                                    jenisSurat.contains("Usaha", ignoreCase = true) -> currentScreen = "surat_usaha"
                                    jenisSurat.contains("Domisili", ignoreCase = true) -> currentScreen = "surat_domisili"
                                    else -> currentScreen = "home"
                                }
                            }
                        )
                    }
                    "detail_pengajuan" -> {
                        DetailPengajuanScreen(
                            jenisSurat = selectedJenisSurat,
                            status = selectedStatusSurat,
                            alasanDitolak = selectedAlasanDitolak,
                            namaFileRt = selectedNamaFileRt,
                            fields = selectedFields,
                            onBackClicked = { currentScreen = "status" },
                            onResubmitClicked = { currentScreen = "home" }
                        )
                    }
                    "profile" -> {
                        ProfileScreen(
                            userName = "Sahal",
                            userFullName = "Sahal Alfarizi",
                            onTabSelected = { tabIndex ->
                                val target = when (tabIndex) {
                                    0 -> "home"
                                    1 -> "status"
                                    else -> "profile"
                                }
                                if (currentScreen != target) {
                                    currentScreen = target
                                    tabHistory.add(target)
                                }
                            },
                            onEditClicked = { currentScreen = "edit_profile" },
                            onLogoutClicked = {
                                currentScreen = "login"
                                tabHistory.clear()
                            },
                            onUbahEmailClicked = { currentScreen = "ubah_email" },
                            onGantiNoHpClicked = { currentScreen = "ganti_nohp" },
                            onGantiPasswordClicked = { currentScreen = "ganti_password" },
                            onVideoTutorialClicked = { currentScreen = "video_tutorial" },
                            onBantuanFaqClicked = { currentScreen = "bantuan_faq" },
                            onTentangAplikasiClicked = { currentScreen = "tentang_aplikasi" }
                        )
                    }
                    "edit_profile" -> {
                        EditProfileScreen(
                            initialNama = "Sahal Alfarizi",
                            onBackClicked = { currentScreen = "profile" },
                            onSaveSuccess = { currentScreen = "profile" }
                        )
                    }
                    "ubah_email" -> UbahEmailScreen(onBackClicked = { currentScreen = "profile" }, onSaveSuccess = { currentScreen = "profile" })
                    "ganti_nohp" -> GantiNoHpScreen(onBackClicked = { currentScreen = "profile" }, onSaveSuccess = { currentScreen = "profile" })
                    "ganti_password" -> GantiPasswordScreen(onBackClicked = { currentScreen = "profile" }, onSaveSuccess = { currentScreen = "profile" })
                    "video_tutorial" -> VideoTutorialScreen(onBackClicked = { currentScreen = "profile" })
                    "bantuan_faq" -> BantuanFaqScreen(onBackClicked = { currentScreen = "profile" })
                    "tentang_aplikasi" -> TentangAplikasiScreen(onBackClicked = { currentScreen = "profile" })
                    "surat_usaha" -> SuratUsahaScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                    "surat_domisili" -> SuratDomisiliScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                    "surat_kehilangan" -> SuratKehilanganScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                    "surat_kematian" -> SuratKematianScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                    "surat_sktm" -> SuratTidakMampuScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                }
            }
        }
    }
}