package com.sudes.purwodadi

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Model data untuk setiap pengajuan surat di Firestore
data class PengajuanSurat(
    val id: String = "",
    val uid: String = "",
    val jenisSurat: String = "",
    val nomorSurat: String = "-",
    val tanggalPengajuan: String = "",
    val status: String = "Proses", // "Proses", "Diterima", "Ditolak"
    val alasanPenolakan: String = "",
    val namaLengkap: String = "",
    val nik: String = "",
    val tempatTanggalLahir: String = "",
    val jenisKelamin: String = "",
    val agama: String = "",
    val statusPerkawinan: String = "",
    val pekerjaan: String = "",
    val alamatLengkap: String = "",
    val keperluanSurat: String = "",
    val lampiranPengantarRt: String = "",
    // Data tambahan spesifik per jenis surat (disimpan fleksibel)
    val dataTambahan: Map<String, String> = emptyMap()
)

class SuratViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _daftarSuratWarga = MutableStateFlow<List<PengajuanSurat>>(emptyList())
    val daftarSuratWarga: StateFlow<List<PengajuanSurat>> = _daftarSuratWarga.asStateFlow()

    private val _daftarSuratAdmin = MutableStateFlow<List<PengajuanSurat>>(emptyList())
    val daftarSuratAdmin: StateFlow<List<PengajuanSurat>> = _daftarSuratAdmin.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * 1. Fungsi Ambil Data Profil Warga untuk tombol "Isi Otomatis dari Profil"
     */
    fun ambilDataProfilWarga(
        onResult: (Map<String, Any>) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onError("Sesi login tidak ditemukan. Silakan masuk kembali.")
            return
        }

        _isLoading.value = true
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                _isLoading.value = false
                if (doc.exists() && doc.data != null) {
                    onResult(doc.data!!)
                } else {
                    onError("Data profil belum diisi di menu Profil.")
                }
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError("Gagal mengambil data profil: ${e.message}")
            }
    }

    /**
     * 2. Fungsi Kirim Pengajuan Surat (Untuk Semua 5 Jenis Surat)
     */
    fun kirimPengajuanSurat(
        jenisSurat: String,
        tanggalPengajuan: String,
        namaLengkap: String,
        nik: String,
        tempatTanggalLahir: String,
        jenisKelamin: String,
        agama: String,
        statusPerkawinan: String,
        pekerjaan: String,
        alamatLengkap: String,
        keperluanSurat: String,
        lampiranPengantarRt: String,
        dataTambahan: Map<String, String> = emptyMap(),
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onError("Anda harus login terlebih dahulu.")
            return
        }

        if (namaLengkap.isBlank() || nik.isBlank() || alamatLengkap.isBlank() || keperluanSurat.isBlank()) {
            onError("Nama, NIK, Alamat, dan Keperluan Surat wajib diisi!")
            return
        }

        _isLoading.value = true
        val docRef = firestore.collection("pengajuan_surat").document()

        val dataSurat = hashMapOf(
            "id" to docRef.id,
            "uid" to userId,
            "jenisSurat" to jenisSurat,
            "nomorSurat" to "-",
            "tanggalPengajuan" to tanggalPengajuan,
            "status" to "Proses",
            "alasanPenolakan" to "",
            "namaLengkap" to namaLengkap,
            "nik" to nik,
            "tempatTanggalLahir" to tempatTanggalLahir,
            "jenisKelamin" to jenisKelamin,
            "agama" to agama,
            "statusPerkawinan" to statusPerkawinan,
            "pekerjaan" to pekerjaan,
            "alamatLengkap" to alamatLengkap,
            "keperluanSurat" to keperluanSurat,
            "lampiranPengantarRt" to lampiranPengantarRt,
            "dataTambahan" to dataTambahan,
            "createdAt" to Timestamp.now()
        )

        docRef.set(dataSurat)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                val msg = "Gagal mengirim pengajuan: ${e.message}"
                _errorMessage.value = msg
                onError(msg)
            }
    }

    /**
     * 3. Fungsi Ambil Riwayat Surat Milik Warga yang Sedang Login (Untuk StatusScreen)
     */
    fun muatRiwayatSuratWarga() {
        val userId = auth.currentUser?.uid ?: return
        _isLoading.value = true

        firestore.collection("pengajuan_surat")
            .whereEqualTo("uid", userId)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) {
                    _errorMessage.value = "Gagal memuat riwayat: ${error.message}"
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toPengajuanSurat()
                } ?: emptyList()
                _daftarSuratWarga.value = list
            }
    }

    /**
     * 4. Fungsi Ambil Semua Pengajuan Warga (Untuk AdminDashboardScreen)
     */
    fun muatSemuaSuratUntukAdmin() {
        _isLoading.value = true

        firestore.collection("pengajuan_surat")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) {
                    _errorMessage.value = "Gagal memuat data pengajuan: ${error.message}"
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toPengajuanSurat()
                } ?: emptyList()
                _daftarSuratAdmin.value = list
            }
    }

    /**
     * 5. Fungsi Admin: Terima atau Tolak Pengajuan Surat
     */
    fun verifikasiSuratAdmin(
        suratId: String,
        nomorSuratResmi: String,
        diterima: Boolean,
        alasanPenolakan: String = "",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true
        val updates = mutableMapOf<String, Any>(
            "status" to if (diterima) "Diterima" else "Ditolak",
            "alasanPenolakan" to if (diterima) "" else alasanPenolakan
        )
        if (nomorSuratResmi.isNotBlank()) {
            updates["nomorSurat"] = nomorSuratResmi
        }

        firestore.collection("pengajuan_surat").document(suratId)
            .update(updates)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError("Gagal memperbarui status surat: ${e.message}")
            }
    }

    /**
     * 6. Fungsi Admin: Edit Data Inputan Warga & Nomor Surat (Untuk AdminEditPengajuanScreen)
     */
    fun editDataSuratAdmin(
        suratId: String,
        nomorSurat: String,
        namaLengkap: String,
        nik: String,
        tempatTanggalLahir: String,
        alamatLengkap: String,
        keperluanSurat: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true
        val updates = mapOf(
            "nomorSurat" to nomorSurat,
            "namaLengkap" to namaLengkap,
            "nik" to nik,
            "tempatTanggalLahir" to tempatTanggalLahir,
            "alamatLengkap" to alamatLengkap,
            "keperluanSurat" to keperluanSurat
        )

        firestore.collection("pengajuan_surat").document(suratId)
            .update(updates)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError("Gagal menyimpan perubahan surat: ${e.message}")
            }
    }

    // Helper konversi dokumen Firestore ke objek PengajuanSurat
    private fun com.google.firebase.firestore.DocumentSnapshot.toPengajuanSurat(): PengajuanSurat {
        @Suppress("UNCHECKED_CAST")
        val tambahan = (get("dataTambahan") as? Map<String, String>) ?: emptyMap()
        return PengajuanSurat(
            id = getString("id") ?: id,
            uid = getString("uid") ?: "",
            jenisSurat = getString("jenisSurat") ?: "",
            nomorSurat = getString("nomorSurat") ?: "-",
            tanggalPengajuan = getString("tanggalPengajuan") ?: "",
            status = getString("status") ?: "Proses",
            alasanPenolakan = getString("alasanPenolakan") ?: "",
            namaLengkap = getString("namaLengkap") ?: "",
            nik = getString("nik") ?: "",
            tempatTanggalLahir = getString("tempatTanggalLahir") ?: "",
            jenisKelamin = getString("jenisKelamin") ?: "",
            agama = getString("agama") ?: "",
            statusPerkawinan = getString("statusPerkawinan") ?: "",
            pekerjaan = getString("pekerjaan") ?: "",
            alamatLengkap = getString("alamatLengkap") ?: "",
            keperluanSurat = getString("keperluanSurat") ?: "",
            lampiranPengantarRt = getString("lampiranPengantarRt") ?: "",
            dataTambahan = tambahan
        )
    }
}