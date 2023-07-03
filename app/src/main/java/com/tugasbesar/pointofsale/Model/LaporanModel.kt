package com.tugasbesar.pointofsale.Model

data class LaporanModel(
    var id: Int,
    var kode: String,
    var nama: String,
    var jenis: String,
    var harga: Int,
    var stok: Int,
    var unit: Int,
    var penjualan: Int,
    var tanggal: Int
)