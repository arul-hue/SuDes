package com.sudes.purwodadi

enum class StatusPengajuan {
    PROSES, DITERIMA, DITOLAK
}

data class PengajuanItem(
    val id: String = "",
    val jenisSurat: String,
    val namaPemohon: String,
    val tanggal: String,
    val status: StatusPengajuan,
    val pesanStatus: String,
    val nik: String = "",
    val tempatTanggalLahir: String = "",
    val jenisKelamin: String = "",
    val agama: String = "",
    val statusPerkawinan: String = "",
    val pekerjaan: String = "",
    val alamatLengkap: String = "",
    val namaUsaha: String = "",
    val lokasiUsaha: String = "",
    val pengantarUri: String = "",
    val nomorSurat: String = "-"
)