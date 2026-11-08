package com.sudes.purwodadi

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // State untuk loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State untuk pesan error atau informasi
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Membersihkan pesan error saat berpindah halaman
     */
    fun clearError() {
        _errorMessage.value = null
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    /**
     * Fungsi registrasi manual melalui Form (Email & Password)
     */
    fun registerUser(
        namaLengkap: String,
        email: String,
        nomorHp: String,
        password: String,
        konfirmasiPassword: String,
        onSuccess: () -> Unit
    ) {
        if (namaLengkap.isBlank() || email.isBlank() || nomorHp.isBlank() || password.isBlank()) {
            _errorMessage.value = "Semua kolom wajib diisi!"
            return
        }

        if (password != konfirmasiPassword) {
            _errorMessage.value = "Kata Sandi dan Konfirmasi Kata Sandi tidak sama!"
            return
        }

        if (password.length < 6) {
            _errorMessage.value = "Kata Sandi minimal harus 6 karakter!"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        // 1. Daftarkan akun ke Firebase Authentication
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: ""

                    // 2. Kirim Email Verifikasi ke email yang didaftarkan
                    firebaseUser?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            // 3. Simpan profil tambahan warga ke Cloud Firestore
                            val userData = hashMapOf(
                                "uid" to userId,
                                "namaLengkap" to namaLengkap,
                                "email" to email,
                                "nomorHp" to nomorHp,
                                "role" to "warga",
                                "createdAt" to com.google.firebase.Timestamp.now()
                            )

                            firestore.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener {
                                    _isLoading.value = false
                                    // Logout sementara agar user wajib verifikasi email dulu sebelum login
                                    auth.signOut()
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    _isLoading.value = false
                                    _errorMessage.value = "Gagal menyimpan data pengguna: ${e.message}"
                                }
                        } else {
                            _isLoading.value = false
                            _errorMessage.value = "Gagal mengirim email verifikasi: ${verificationTask.exception?.message}"
                        }
                    }
                } else {
                    _isLoading.value = false
                    val rawMessage = task.exception?.message ?: ""

                    _errorMessage.value = when {
                        rawMessage.contains("email address is already in use", ignoreCase = true) ->
                            "Email ini sudah terdaftar. Silakan gunakan email lain atau masuk ke akun Anda."
                        rawMessage.contains("badly formatted", ignoreCase = true) ->
                            "Format penulisan email salah atau tidak valid."
                        rawMessage.contains("weak password", ignoreCase = true) ->
                            "Kata sandi terlalu lemah. Gunakan minimal 6 karakter."
                        else ->
                            "Pendaftaran gagal: $rawMessage"
                    }
                }
            }
    }

    /**
     * Fungsi login manual yang mengambil data role dari Firestore
     */
    fun loginUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit // Menerima parameter role (String)
    ) {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Email dan kata sandi wajib diisi!"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _errorMessage.value = "Format penulisan email salah atau tidak valid."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    android.util.Log.d("DEBUG_LOGIN", "UID yang sedang login: $userId")

                    if (userId != null) {
                        // Ambil role dari Firestore berdasarkan UID user yang login
                        firestore.collection("users").document(userId).get()
                            .addOnSuccessListener { document ->
                                _isLoading.value = false
                                android.util.Log.d("DEBUG_LOGIN", "Apakah dokumen ada? ${document.exists()}")
                                android.util.Log.d("DEBUG_LOGIN", "Role tertulis di Firestore: ${document.getString("role")}")

                                val role = document.getString("role") ?: "user"
                                onSuccess(role) // Kirim role ke MainActivity
                            }
                            .addOnFailureListener { e ->
                                _isLoading.value = false
                                android.util.Log.e("DEBUG_LOGIN", "Gagal ambil Firestore: ${e.message}")
                                _errorMessage.value = "Gagal memuat data role: ${e.message}"
                            }
                    } else {
                        _isLoading.value = false
                        _errorMessage.value = "Sesi pengguna tidak valid."
                    }
                } else {
                    _isLoading.value = false
                    val rawMessage = task.exception?.message ?: ""

                    _errorMessage.value = when {
                        rawMessage.contains("password", ignoreCase = true) ||
                                rawMessage.contains("credential", ignoreCase = true) ||
                                rawMessage.contains("wrong-password", ignoreCase = true) ||
                                rawMessage.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) ||
                                rawMessage.contains("user-not-found", ignoreCase = true) ||
                                rawMessage.contains("no user record", ignoreCase = true) ->
                            "Email atau kata sandi yang Anda masukkan salah."

                        rawMessage.contains("network", ignoreCase = true) ->
                            "Koneksi internet bermasalah. Periksa kembali sambungan Anda."

                        else ->
                            "Gagal masuk: $rawMessage"
                    }
                }
            }
    }

    /**
     * Fungsi untuk autentikasi dan penyimpanan data jika warga masuk/daftar menggunakan Google
     */
    fun signInWithGoogleCredential(
        idToken: String,
        namaLengkap: String,
        email: String,
        onSuccess: () -> Unit
    ) {
        _isLoading.value = true
        _errorMessage.value = null

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: ""

                    val userData = hashMapOf(
                        "uid" to userId,
                        "namaLengkap" to namaLengkap,
                        "email" to email,
                        "role" to "warga",
                        "createdAt" to com.google.firebase.Timestamp.now()
                    )

                    firestore.collection("users").document(userId)
                        .set(userData, SetOptions.merge())
                        .addOnCompleteListener { _ ->
                            _isLoading.value = false
                            onSuccess()
                        }
                } else {
                    _isLoading.value = false
                    val rawMessage = task.exception?.message ?: "Terjadi kesalahan sistem"

                    _errorMessage.value = when {
                        rawMessage.contains("network", ignoreCase = true) ->
                            "Koneksi internet bermasalah. Periksa kembali sambungan Anda."
                        else ->
                            "Autentikasi Google gagal: $rawMessage"
                    }
                }
            }
    }

    /**
     * Fungsi untuk mengirim tautan reset password ke email warga
     */
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank()) {
            _errorMessage.value = "Email wajib diisi!"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _errorMessage.value = "Format penulisan email salah atau tidak valid."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        auth.sendPasswordResetEmail(email.trim())
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val rawMessage = task.exception?.message ?: ""
                    val errorCode = (task.exception as? com.google.firebase.auth.FirebaseAuthException)?.errorCode ?: ""

                    _errorMessage.value = when {
                        errorCode == "ERROR_USER_NOT_FOUND" ||
                                rawMessage.contains("user-not-found", ignoreCase = true) ||
                                rawMessage.contains("no user record", ignoreCase = true) ->
                            "Email ini belum terdaftar di sistem."
                        rawMessage.contains("network", ignoreCase = true) ->
                            "Koneksi internet bermasalah. Periksa kembali sambungan Anda."
                        else ->
                            "Gagal mengirim reset password: $rawMessage"
                    }
                }
            }
    }
}