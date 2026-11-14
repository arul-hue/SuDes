package com.sudes.purwodadi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.sudes.purwodadi.ui.theme.*

class MainActivity : ComponentActivity() {
    private val authViewModel by lazy { AuthViewModel() }
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Fungsi Helper untuk Menyimpan Sesi Role Admin/Warga di Lokal HP
    private fun saveUserRole(role: String) {
        val sharedPreferences = getSharedPreferences("SudesPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("USER_ROLE", role).apply()
    }

    private fun getSavedUserRole(): String {
        val sharedPreferences = getSharedPreferences("SudesPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("USER_ROLE", "warga") ?: "warga"
    }

    private fun clearUserRole() {
        val sharedPreferences = getSharedPreferences("SudesPrefs", MODE_PRIVATE)
        sharedPreferences.edit().remove("USER_ROLE").apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudesTheme {
                var currentScreen by remember { mutableStateOf("splash") }
                val isLoading by authViewModel.isLoading.collectAsState()

                // State Profil Pengguna yang Aktif
                var namaLengkapUser by remember { mutableStateOf("Warga") }
                var emailUser by remember { mutableStateOf("") }
                var nikUser by remember { mutableStateOf("") }
                var tempatTglLahirUser by remember { mutableStateOf("") }
                var pekerjaanUser by remember { mutableStateOf("") }
                var agamaUser by remember { mutableStateOf("") }
                var statusUser by remember { mutableStateOf("") }
                var alamatUser by remember { mutableStateOf("") }
                var roleUser by remember { mutableStateOf("warga") }

                var showIncompleteProfileDialog by remember { mutableStateOf(false) }

                // SINKRONISASI REAL-TIME AMAN & SESUAI MODEL PENGAJUAN
                val listPengajuan = remember { mutableStateListOf<PengajuanItem>() }

                LaunchedEffect(Unit) {
                    firestore.collection("pengajuan")
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) return@addSnapshotListener
                            if (snapshot != null) {
                                val tempList = mutableListOf<PengajuanItem>()
                                for (doc in snapshot.documents) {
                                    try {
                                        val item = doc.toObject(PengajuanItem::class.java)
                                        if (item != null) {
                                            val finalItem = if (item.id.isBlank()) item.copy(id = doc.id) else item
                                            tempList.add(finalItem)
                                        }
                                    } catch (ex: Exception) {
                                        try {
                                            val id = doc.getString("id") ?: doc.id
                                            val jenisSurat = doc.getString("jenisSurat") ?: "Surat Keterangan Usaha"
                                            val namaPemohon = doc.getString("namaPemohon") ?: "Warga"
                                            val tanggal = doc.getString("tanggal") ?: ""
                                            val statusStr = doc.getString("status") ?: "PROSES"
                                            val status = try { StatusPengajuan.valueOf(statusStr) } catch (err: Exception) { StatusPengajuan.PROSES }
                                            val pesanStatus = doc.getString("pesanStatus") ?: "Pengajuan Anda sedang diproses."

                                            val nik = doc.getString("nik") ?: ""
                                            val tempatTanggalLahir = doc.getString("tempatTanggalLahir") ?: ""
                                            val jenisKelamin = doc.getString("jenisKelamin") ?: ""
                                            val agama = doc.getString("agama") ?: ""
                                            val statusPerkawinan = doc.getString("statusPerkawinan") ?: ""
                                            val pekerjaan = doc.getString("pekerjaan") ?: ""
                                            val alamatLengkap = doc.getString("alamatLengkap") ?: ""
                                            val namaUsaha = doc.getString("namaUsaha") ?: ""
                                            val lokasiUsaha = doc.getString("lokasiUsaha") ?: ""
                                            val pengantarUri = doc.getString("pengantarUri") ?: ""
                                            val nomorSurat = doc.getString("nomorSurat") ?: "-"

                                            val fallbackItem = PengajuanItem(
                                                id = id,
                                                jenisSurat = jenisSurat,
                                                namaPemohon = namaPemohon,
                                                tanggal = tanggal,
                                                status = status,
                                                pesanStatus = pesanStatus,
                                                nik = nik,
                                                tempatTanggalLahir = tempatTanggalLahir,
                                                jenisKelamin = jenisKelamin,
                                                agama = agama,
                                                statusPerkawinan = statusPerkawinan,
                                                pekerjaan = pekerjaan,
                                                alamatLengkap = alamatLengkap,
                                                namaUsaha = namaUsaha,
                                                lokasiUsaha = lokasiUsaha,
                                                pengantarUri = pengantarUri,
                                                nomorSurat = nomorSurat
                                            )
                                            tempList.add(fallbackItem)
                                        } catch (innerEx: Exception) {
                                            // Abaikan dokumen korup
                                        }
                                    }
                                }
                                listPengajuan.clear()
                                listPengajuan.addAll(tempList)
                            }
                        }
                }

                val fetchUserData = {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        firestore.collection("users").document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    namaLengkapUser = document.getString("namaLengkap") ?: (auth.currentUser?.displayName ?: "Warga")
                                    emailUser = document.getString("email") ?: (auth.currentUser?.email ?: "")

                                    nikUser = document.getString("nik")?.takeIf { it != "-" } ?: ""
                                    tempatTglLahirUser = document.getString("tempatTanggalLahir")?.takeIf { it != "-" } ?: ""
                                    pekerjaanUser = document.getString("pekerjaan")?.takeIf { it != "-" } ?: ""
                                    agamaUser = document.getString("agama")?.takeIf { it != "-" } ?: ""
                                    statusUser = document.getString("statusPerkawinan")?.takeIf { it != "-" } ?: ""
                                    alamatUser = document.getString("alamatLengkap")?.takeIf { it != "-" } ?: ""

                                    roleUser = document.getString("role") ?: "warga"
                                    saveUserRole(roleUser)

                                    val isNikEmpty = nikUser.isBlank()
                                    val isTglEmpty = tempatTglLahirUser.isBlank()

                                    if (roleUser != "admin" && (isNikEmpty || isTglEmpty)) {
                                        showIncompleteProfileDialog = true
                                    }
                                }
                            }
                    }
                }

                val tabHistory = remember { mutableStateListOf("home") }
                var backPressedTime by remember { mutableStateOf(0L) }

                var selectedPengajuanItem by remember { mutableStateOf<PengajuanItem?>(null) }
                var itemYangAkanDiedit by remember { mutableStateOf<PengajuanItem?>(null) }

                var selectedJenisSurat by remember { mutableStateOf("Surat Keterangan Usaha") }
                var selectedStatusSurat by remember { mutableStateOf(StatusPengajuan.PROSES) }
                var selectedAlasanDitolak by remember { mutableStateOf<String?>(null) }
                var selectedNamaFileRt by remember { mutableStateOf("pengantar_rt.jpg") }

                var selectedFields by remember {
                    mutableStateOf(
                        listOf(
                            DetailFieldItem("Nama Lengkap", namaLengkapUser),
                            DetailFieldItem("NIK", nikUser),
                            DetailFieldItem("Tempat, Tanggal Lahir", tempatTglLahirUser),
                            DetailFieldItem("Jenis Kelamin", "Laki-laki"),
                            DetailFieldItem("Agama", agamaUser),
                            DetailFieldItem("Status Perkawinan", statusUser),
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

                Box(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        "splash" -> {
                            SplashScreen(
                                onTimeout = {
                                    if (auth.currentUser != null) {
                                        fetchUserData()
                                        val savedRole = getSavedUserRole()
                                        if (savedRole == "admin") {
                                            currentScreen = "admin_dashboard"
                                        } else {
                                            currentScreen = "home"
                                        }
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
                                            val savedRole = getSavedUserRole()
                                            if (savedRole == "admin") {
                                                currentScreen = "admin_dashboard"
                                            } else {
                                                currentScreen = "home"
                                                tabHistory.clear()
                                                tabHistory.add("home")
                                            }
                                        }
                                    )
                                },
                                onLoginSuccess = { role ->
                                    val cleanedRole = role.trim().lowercase()
                                    fetchUserData()
                                    saveUserRole(cleanedRole)

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
                                listPengajuan = listPengajuan,
                                onDetailClicked = { pengajuanItem ->
                                    // 👈 Mengarahkan admin ke layar detail khusus admin
                                    selectedPengajuanItem = pengajuanItem
                                    currentScreen = "admin_detail_pengajuan"
                                },
                                onSelesaikanClicked = { pengajuanItem ->
                                    // Mengarahkan ke form edit/keputusan admin
                                    selectedPengajuanItem = pengajuanItem
                                    selectedJenisSurat = pengajuanItem.jenisSurat
                                    currentScreen = "admin_edit_pengajuan"
                                },
                                onLogoutClicked = {
                                    auth.signOut()
                                    clearUserRole()
                                    currentScreen = "login"
                                    tabHistory.clear()
                                }
                            )
                        }
                        "admin_detail_pengajuan" -> {
                            // 👈 Layar Detail Pengajuan sisi Admin
                            if (selectedPengajuanItem != null) {
                                AdminDetailPengajuanScreen(
                                    pengajuanItem = selectedPengajuanItem!!,
                                    onBackClicked = { currentScreen = "admin_dashboard" },
                                    onSelesaikanClicked = { pengajuanItem ->
                                        selectedPengajuanItem = pengajuanItem
                                        selectedJenisSurat = pengajuanItem.jenisSurat
                                        currentScreen = "admin_edit_pengajuan"
                                    }
                                )
                            }
                        }
                        "admin_edit_pengajuan" -> {
                            if (selectedPengajuanItem != null) {
                                AdminEditPengajuanScreen(
                                    pengajuan = selectedPengajuanItem!!,
                                    onBackClicked = { currentScreen = "admin_dashboard" },
                                    onSaveSuccess = { updatedItem ->
                                        firestore.collection("pengajuan").document(updatedItem.id)
                                            .set(updatedItem)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Keputusan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                                currentScreen = "admin_dashboard"
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Gagal menyimpan: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                )
                            }
                        }
                        "home" -> {
                            HomeScreen(
                                namaLengkap = namaLengkapUser,
                                onServiceSelected = { serviceName ->
                                    itemYangAkanDiedit = null
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
                                listPengajuan = listPengajuan,
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
                                onDetailClicked = { pengajuanItem ->
                                    selectedPengajuanItem = pengajuanItem
                                    selectedJenisSurat = pengajuanItem.jenisSurat
                                    selectedStatusSurat = pengajuanItem.status
                                    selectedAlasanDitolak = pengajuanItem.pesanStatus
                                    selectedNamaFileRt = pengajuanItem.pengantarUri.ifBlank { "pengantar_rt.jpg" }

                                    val baseFields = mutableListOf(
                                        DetailFieldItem("Nama Lengkap", pengajuanItem.namaPemohon),
                                        DetailFieldItem("NIK", pengajuanItem.nik.ifBlank { "-" }),
                                        DetailFieldItem("Tempat, Tanggal Lahir", pengajuanItem.tempatTanggalLahir.ifBlank { "-" }),
                                        DetailFieldItem("Jenis Kelamin", pengajuanItem.jenisKelamin.ifBlank { "-" }),
                                        DetailFieldItem("Agama", pengajuanItem.agama.ifBlank { "-" }),
                                        DetailFieldItem("Status Perkawinan", pengajuanItem.statusPerkawinan.ifBlank { "-" }),
                                        DetailFieldItem("Pekerjaan", pengajuanItem.pekerjaan.ifBlank { "-" }),
                                        DetailFieldItem("Alamat Lengkap", pengajuanItem.alamatLengkap.ifBlank { "-" })
                                    )

                                    if (pengajuanItem.jenisSurat.contains("Usaha", ignoreCase = true)) {
                                        baseFields.add(DetailFieldItem("Nama / Deskripsi Usaha", pengajuanItem.namaUsaha.ifBlank { "-" }))
                                        baseFields.add(DetailFieldItem("Lokasi Usaha", pengajuanItem.lokasiUsaha.ifBlank { "-" }))
                                    }

                                    selectedFields = baseFields
                                    currentScreen = "detail_pengajuan"
                                },
                                onResubmitClicked = { pengajuanItem ->
                                    itemYangAkanDiedit = pengajuanItem
                                    if (pengajuanItem.jenisSurat.contains("Usaha", ignoreCase = true)) {
                                        currentScreen = "surat_usaha"
                                    }
                                }
                            )
                        }
                        "detail_pengajuan" -> {
                            DetailPengajuanScreen(
                                jenisSurat = selectedJenisSurat,
                                status = selectedStatusSurat,
                                tanggalPengajuan = selectedPengajuanItem?.tanggal ?: "",
                                alasanDitolak = selectedAlasanDitolak,
                                namaFileRt = if (!selectedPengajuanItem?.pengantarUri.isNullOrBlank()) "pengantar_rt_warga.jpg" else "Tidak ada lampiran",
                                pengantarUriString = selectedPengajuanItem?.pengantarUri ?: "",
                                fields = selectedFields,
                                onBackClicked = { currentScreen = "status" },
                                onResubmitClicked = {
                                    if (selectedPengajuanItem != null) {
                                        itemYangAkanDiedit = selectedPengajuanItem
                                    }
                                    if (selectedJenisSurat.contains("Usaha", ignoreCase = true)) {
                                        currentScreen = "surat_usaha"
                                    }
                                }
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
                                    clearUserRole()
                                    currentScreen = "login"
                                    tabHistory.clear()
                                },
                                onGantiEmailClicked = { currentScreen = "ganti_email" },
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
                        "ganti_email" -> GantiEmailScreen(
                            onBackClicked = { currentScreen = "profile" },
                            onSaveSuccess = { currentScreen = "profile" },
                            onLogoutRequested = {
                                auth.signOut()
                                clearUserRole()
                                currentScreen = "login"
                                tabHistory.clear()
                            }
                        )
                        "ganti_password" -> GantiPasswordScreen(onBackClicked = { currentScreen = "profile" }, onSaveSuccess = { currentScreen = "profile" })
                        "video_tutorial" -> VideoTutorialScreen(onBackClicked = { currentScreen = "profile" })
                        "bantuan_faq" -> BantuanFaqScreen(onBackClicked = { currentScreen = "profile" })
                        "tentang_aplikasi" -> TentangAplikasiScreen(onBackClicked = { currentScreen = "profile" })

                        "surat_usaha" -> SuratUsahaScreen(
                            initialNama = namaLengkapUser,
                            initialNik = nikUser,
                            initialTempatTgl = tempatTglLahirUser,
                            initialAgama = agamaUser,
                            initialStatusPerkawinan = statusUser,
                            initialPekerjaan = pekerjaanUser,
                            initialAlamat = alamatUser,
                            existingItem = itemYangAkanDiedit,
                            onBackClicked = {
                                itemYangAkanDiedit = null
                                currentScreen = "home"
                            },
                            onSubmitPengajuan = { itemBaru ->
                                firestore.collection("pengajuan").document(itemBaru.id)
                                    .set(itemBaru)
                                    .addOnSuccessListener {
                                        if (itemYangAkanDiedit != null && itemYangAkanDiedit!!.id != itemBaru.id) {
                                            firestore.collection("pengajuan").document(itemYangAkanDiedit!!.id).delete()
                                        }
                                        itemYangAkanDiedit = null
                                        currentScreen = "status"
                                        Toast.makeText(context, "Pengajuan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Gagal mengirim: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        )
                        "surat_domisili" -> SuratDomisiliScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                        "surat_kehilangan" -> SuratKehilanganScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                        "surat_kematian" -> SuratKematianScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                        "surat_sktm" -> SuratTidakMampuScreen(onBackClicked = { currentScreen = "home" }, onSubmitSuccess = { currentScreen = "home" })
                    }

                    if (showIncompleteProfileDialog) {
                        AlertDialog(
                            onDismissRequest = { },
                            containerColor = Color.White,
                            shape = RoundedCornerShape(20.dp),
                            title = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(
                                        modifier = Modifier.size(56.dp),
                                        shape = CircleShape,
                                        color = Color(0xFFFEF3C7)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = "Peringatan",
                                                tint = Color(0xFFD97706),
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Data Diri Belum Lengkap",
                                        style = AppTypography.HeadingMedium,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            },
                            text = {
                                Text(
                                    text = "Halo $namaLengkapUser, silakan lengkapi data diri Anda terlebih dahulu agar dapat mengajukan layanan surat dengan lancar.",
                                    style = AppTypography.BodyRegular,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            confirmButton = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            showIncompleteProfileDialog = false
                                            currentScreen = "edit_profile"
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GreenDesaPrimary)
                                    ) {
                                        Text(text = "Lengkapi Sekarang", style = AppTypography.ButtonText)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedButton(
                                        onClick = {
                                            showIncompleteProfileDialog = false
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(text = "Nanti Saja", color = TextPrimary, style = AppTypography.ButtonText)
                                    }
                                }
                            },
                            dismissButton = {}
                        )
                    }
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
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                firestore.collection("users").document(userId).get().addOnSuccessListener { doc ->
                                    val role = doc.getString("role")?.trim()?.lowercase() ?: "warga"
                                    saveUserRole(role)
                                    authViewModel.setLoading(false)
                                    Toast.makeText(this@MainActivity, "Berhasil masuk dengan Google!", Toast.LENGTH_SHORT).show()
                                    onSuccess()
                                }.addOnFailureListener {
                                    authViewModel.setLoading(false)
                                    onSuccess()
                                }
                            } else {
                                authViewModel.setLoading(false)
                                onSuccess()
                            }
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