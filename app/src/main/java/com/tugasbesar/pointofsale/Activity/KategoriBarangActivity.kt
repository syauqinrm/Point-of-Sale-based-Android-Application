package com.tugasbesar.pointofsale.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.pos.Activity.formBarang
import com.example.pos.R



class KategoriBarangActivity : AppCompatActivity() {
    // Barang yang dipilih
    var selectedItem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori_barang)

        // Referensi button
        val BackBtn :ImageButton = findViewById(R.id.BackBtn)
        val Kertas :Button = findViewById(R.id.Kertas)
        val AlatTulis :Button = findViewById(R.id.AlatTulis)
        val Buku :Button = findViewById(R.id.Buku)
        val Sampul :Button = findViewById(R.id.Sampul)
        val KotakPensil :Button = findViewById(R.id.KotakPensil)
        val Aksesoris :Button = findViewById(R.id.Aksesoris)
        val Lainnya :Button = findViewById(R.id.Lainnya)

        Kertas.setOnClickListener() {
            selectedItem = "Kertas"
            goBack()
        }

        AlatTulis.setOnClickListener() {
            selectedItem = "Alat Tulis"
            goBack()
        }

        Buku.setOnClickListener() {
            selectedItem = "Buku"
            goBack()
        }

        Sampul.setOnClickListener() {
            selectedItem = "Sampul"
            goBack()
        }

        KotakPensil.setOnClickListener() {
            selectedItem = "Kotak Pensil"
            goBack()
        }

        Aksesoris.setOnClickListener() {
            selectedItem = "Aksesoris"
            goBack()
        }

        Lainnya.setOnClickListener() {
            selectedItem = "Lainnya"
            goBack()
        }

        // Tombol back di pojok kiri
        BackBtn.setOnClickListener() {
            val next = Intent(this, formBarang::class.java)
            startActivity(next)
        }

    }

    // Jika barang sudah dipilih maka kembali ke sebelumnya
    fun goBack() {
        val next = Intent(this, formBarang::class.java)
        next.putExtra("Barang",selectedItem)
        startActivity(next)
    }
}