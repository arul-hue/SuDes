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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.sudes.purwodadi.ui.theme.SudesTheme

class MainActivity : ComponentActivity() {
    private val authViewModel by lazy { AuthViewModel() }
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudesTheme {
                var currentScreen by remember { mutableStateOf("splash") }
                val isLoading by authViewModel.isLoading.collectAsState()

                // State Dinamis untuk Profil Pengguna (Murni menggunakan namaLengkap)
                var namaLengkapUser by remember { mutableStateOf("Warga") }
                var emailUser by remember { mutableStateOf("") }
                var nikUser by remember { mutableStateOf("") }
                var tempatTglLahirUser by remember { mutableStateOf("") }
                var pekerjaanUser by remember { mutableStateOf("") }
                var agamaUser by remember { mutableStateOf("") }
                var statusUser by remember { mutableStateOf("") }
                var alamatUser by remember { mutableStateOf("") }
                var roleUser by remember { mutableStateOf("warga") }

                // Fungsi untuk mengambil data terbaru dari Firestore
                val fetchUserData = {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        firestore.collection("users").document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    namaLengkapUser = document.getString("namaLengkap") ?: (auth.currentUser?.displayName ?: "Warga")
                                    emailUser = document.getString("email") ?: (auth.currentUser?.email ?: "")
                                    nikUser = document.getString("nik") ?: ""
                                    tempatTglLahirUser = document.getString("tempatTanggalLahir") ?: ""
                                    pekerjaanUser = document.getString("pekerjaan") ?: ""
                                    agamaUser = document.getString("agama") ?: ""
                                    statusUser = document.getString("statusPerkawinan") ?: ""
                                    alamatUser = document.getString("alamatLengkap") ?: ""
                                    roleUser = document.getString("role") ?: "warga"
                                }
                            }
                    }
                }

                // RIWAYAT TAB GLOBAL
                val tabHistory = remember { mutableStateListOf("home") }
                var backPressedTime by remember { mutableStateOf(0L) }

                // State untuk surat
                var selectedJenisSurat by remember { mutableStateOf("Surat Keterangan Domisili") }
                var selectedStatusSurat by remember { mutableStateOf(StatusPengajuan.PROSES) }
                var selectedAlasanDitolak by remember { mutableStateOf<String?>(null) }
                var selectedNamaFileRt by remember { mutableStateOf("pengantar_rt03.jpg") }

                var selectedFields by remember {
                    mutableStateOf(
                        listOf(
                            DetailFieldItem("Nama Lengkap", namaLengkapUser),
                            DetailFieldItem("NIK", nikUser),
                            DetailFieldItem("Tempat, Tanggal Lahir", tempatTglLahirUser),
                            DetailFieldItem("Jenis Kelamin", "Laki-laki"),
                            DetailFieldItem("Agama", agamaUser),
                            DetailFieldItem("Pekerjaan", pekerjaanUser),
                            DetailFieldItem("Alamat Lengkap", alamatUser)
                        )
                    )
                }

                val context = this

                if (currentScreen == "home" || currentScreen == "status" || currentScreen == "profile") {
                    BackHandler {
                        if (tabHistory.size > 1) {
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
                            onTimeout = {
                                if (auth.currentUser != null) {
                                    fetchUserData()
                                    currentScreen = "home"
                                } else {
                                    currentScreen = "login"
                                }
                            }
                        )
                    }
                    "login" -> {
                        LoginScreen(
                            authViewModel = authViewModel,
                            isLoading = isLoading,
                            onGoogleLoginClicked = {
                                handleGoogleSignIn(
                                    onSuccess = {
                                        fetchUserData()
                                        currentScreen = "home"
                                        tabHistory.clear()
                                        tabHistory.add("home")
                                    }
                                )
                            },
                            onLoginSuccess = { role ->
                                val cleanedRole = role.trim().lowercase()
                                fetchUserData()
                                if (cleanedRole == "admin") {
                                    currentScreen = "admin_dashboard"
                                    Toast.makeText(context, "Berhasil masuk sebagai Admin!", Toast.LENGTH_SHORT).show()
                                } else {
                                    currentScreen = "home"
                                    tabHistory.clear()
                                    tabHistory.add("home")
                                    Toast.makeText(context, "Berhasil masuk!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onRegisterClicked = {
                                authViewModel.clearError()
                                currentScreen = "register"
                            },
                            onForgotPasswordClicked = {
                                authViewModel.clearError()
                                currentScreen = "forgot_password"
                            }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            authViewModel = authViewModel,
                            isLoading = isLoading,
                            onGoogleRegisterClicked = {
                                handleGoogleSignIn(
                                    onSuccess = {
                                        fetchUserData()
                                        currentScreen = "home"
                                        tabHistory.clear()
                                        tabHistory.add("home")
                                    }
                                )
                            },
                            onBackToLoginClicked = {
                                authViewModel.clearError()
                                currentScreen = "login"
                            }
                        )
                    }
                    "forgot_password" -> {
                        ForgotPasswordScreen(
                            authViewModel = authViewModel,
                            isLoading = isLoading,
                            onBackToLoginClicked = {
                                authViewModel.clearError()
                                currentScreen = "login"
                            }
                        )
                    }
                    "admin_dashboard" -> {
                        AdminDashboardScreen(
                            onDetailClicked = { _ -> },
                            onSelesaikanClicked = { currentScreen = "admin_edit_pengajuan" },
                            onLogoutClicked = {
                                auth.signOut()
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
                            namaLengkap = namaLengkapUser,
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
                            namaLengkap = namaLengkapUser,
                            userRole = if (roleUser == "admin") "Administrator Desa Purwodadi Simpang" else "Warga Desa Purwodadi Simpang",
                            nik = nikUser,
                            tempatTanggalLahir = tempatTglLahirUser,
                            pekerjaan = pekerjaanUser,
                            agama = agamaUser,
                            statusPerkawinan = statusUser,
                            alamatLengkap = alamatUser,
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
                                auth.signOut()
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
                            authViewModel = authViewModel,
                            initialNama = namaLengkapUser,
                            initialNik = nikUser,
                            initialTempatTgl = tempatTglLahirUser,
                            initialPekerjaan = pekerjaanUser,
                            initialAgama = agamaUser,
                            initialStatus = statusUser,
                            initialAlamat = alamatUser,
                            onBackClicked = {
                                fetchUserData()
                                currentScreen = "profile"
                            },
                            onSaveSuccess = {
                                fetchUserData()
                                currentScreen = "profile"
                            }
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

    private fun handleGoogleSignIn(onSuccess: () -> Unit) {
        if (authViewModel.isLoading.value) return
        authViewModel.setLoading(true)

        val credentialManager = CredentialManager.create(this)
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

                    authViewModel.signInWithGoogleCredential(
                        idToken = googleIdToken,
                        namaLengkap = googleName,
                        email = googleEmail,
                        onSuccess = {
                            authViewModel.setLoading(false)
                            Toast.makeText(this@MainActivity, "Berhasil masuk dengan Google!", Toast.LENGTH_SHORT).show()
                            onSuccess()
                        }
                    )
                } else {
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
                authViewModel.setLoading(false)
            }
        }
    }
}