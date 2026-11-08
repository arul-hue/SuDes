package com.sudes.purwodadi

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
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

    // Di dalam class AuthViewModel.kt Anda, pastikan ada fungsi ini:
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
            _errorMessage.value = "Password dan Konfirmasi Password tidak sama!"
            return
        }

        if (password.length < 8) {
            _errorMessage.value = "Password minimal harus 8 karakter!"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        // 1. Daftarkan akun ke Firebase Authentication
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""

                    // 2. Simpan profil tambahan warga ke Cloud Firestore
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
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            _isLoading.value = false
                            _errorMessage.value = "Gagal menyimpan data pengguna: ${e.message}"
                        }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.message ?: "Pendaftaran gagal, silakan coba lagi."
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
                    val userId = auth.currentUser?.uid ?: ""
                    val userRef = firestore.collection("users").document(userId)

                    // Cek apakah data user sudah ada di Firestore, jika belum buat dokumen baru
                    userRef.get().addOnSuccessListener { document ->
                        if (!document.exists()) {
                            val userData = hashMapOf(
                                "uid" to userId,
                                "namaLengkap" to namaLengkap,
                                "email" to email,
                                "nomorHp" to "", // Google Sign-In tidak membawa nomor HP secara otomatis
                                "role" to "warga",
                                "createdAt" to com.google.firebase.Timestamp.now()
                            )
                            userRef.set(userData)
                                .addOnSuccessListener {
                                    _isLoading.value = false
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    _isLoading.value = false
                                    _errorMessage.value = "Gagal menyimpan data Google ke Firestore: ${e.message}"
                                }
                        } else {
                            _isLoading.value = false
                            onSuccess()
                        }
                    }.addOnFailureListener { e ->
                        _isLoading.value = false
                        _errorMessage.value = "Gagal memeriksa data user: ${e.message}"
                    }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.message ?: "Autentikasi Google gagal."
                }
            }
    }
}