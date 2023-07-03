package com.example.pos.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.example.pos.RecycleView.CustomAdapter
import com.example.pos.RecycleView.CustomAdapter.OnItemsClickListener
import java.nio.BufferUnderflowException


class MainActivity : AppCompatActivity() {
    lateinit var settingsFAB: CardView
    lateinit var adapter: CustomAdapter
    lateinit var search: SearchView
    lateinit var recyclerview: RecyclerView
    lateinit var frameRefresh: FrameLayout
    lateinit var totalBeli: TextView
    lateinit var listitem: ArrayList<model_barang>
    var nama_arr:ArrayList<String> = ArrayList()
    var total: Int = 0
    lateinit var modelItemx:ArrayList<model_barang>
    var arr_kode: ArrayList<String> = ArrayList()
    var arr_harga = HashMap<String, Int>()
    private var arr_nama: ArrayList<String> = ArrayList()
    var arr_jenis = HashMap<String, String>()
    var arr_jumlah = HashMap<String, Int>()
    var arr_stok2 = HashMap<String, Int>()
    var bundle:Bundle = Bundle()
    var bundle1:Bundle = Bundle()
    var bundle2:Bundle = Bundle()

    lateinit var btn_bayar: ImageView
    private lateinit var sharedPref_total: SharedPreferences.Editor
    private lateinit var sharedJumlah: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var log:Intent
    private lateinit var list_kode:String
    private lateinit var list_nama:String
    var list_harga:Int = 0
    var list_stok:Int = 0
    private lateinit var list_jenis:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var kembali: ImageView= findViewById(R.id.kembali)
        recyclerview = findViewById<RecyclerView>(R.id.listBarang)
        totalBeli = findViewById(R.id.total_beli)
        settingsFAB = findViewById(R.id.setting)
        recyclerview.setHasFixedSize(true)
        frameRefresh = findViewById(R.id.refresh)
        btn_bayar = findViewById(R.id.keranjang)
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        search = findViewById(R.id.search_all)
        listitem = readAll()
        adapter = getAdapter2() //adapterBarang
        recyclerview.adapter = adapter

        //sharedPreferences
        sharedPref_total = getSharedPreferences("key", MODE_PRIVATE).edit()
        sharedJumlah = applicationContext.getSharedPreferences("key_item", 0)
        editor = sharedJumlah.edit()

        settingsFAB.setOnClickListener {
            intent = Intent(this, form_login::class.java)
            startActivity(intent).also {
                total = 0
                totalBeli.text = "0"
            }
        }
        try {
            var hrg:Int? = savedInstanceState!!.getInt("Key_Harga_Save")
            val bundle1_2:Bundle = intent.getBundleExtra("Data_Jumlah")!!
            val bundle2_2:Bundle = intent.getBundleExtra("Data_Stok")!!
            val bundle3:Bundle = intent.getBundleExtra("Data_Harga")!!

            for (i in nama_arr){

                if(bundle1 != null ){

                    bundle = bundle1_2
                    bundle1 = bundle2_2
                    total = hrg!!
                    list_stok = bundle2.getInt(i)

                }
            }

        } catch (e:Exception){
        }
        kembali.setOnClickListener {
            finishAffinity()
        }
        Toast.makeText(this, "Data list_jumlah : ${bundle.toString()}\nData list_stok : ${bundle1.toString()}",Toast.LENGTH_LONG).show()
        //search view query
        search.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
        adapter.notifyDataSetChanged()

        if(savedInstanceState != null){
            var bnd_jml: Bundle? = savedInstanceState.getBundle("LIST_JUMLAH")
            var bnd_stok:Bundle? = savedInstanceState.getBundle("LIST_STOK")
        }

        kembali.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun readAll(): ArrayList<model_barang> {
        val db: Database = Database(this)
        var cursor: Cursor = db.viewBarang()
        modelItemx = ArrayList<model_barang>()
        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                list_kode = cursor.getString(0)
                list_nama = cursor.getString(1)
                list_harga = cursor.getInt(2)
                list_jenis = cursor.getString(3)
                list_stok = cursor.getInt(4)
                modelItemx.add(
                    model_barang(
                        list_kode,
                        list_nama,
                        list_harga,
                        list_jenis,
                        list_stok,
                        totalBeli.text.toString()
                    )
                )
                nama_arr.add(list_nama)
            }
        }

        return modelItemx
    }

    private fun getAdapter2(): CustomAdapter {
        adapter = CustomAdapter(this, readAll(),bundle)
        adapter.setWhenClickListener(object : OnItemsClickListener {

            override fun onItemClick(harga: Int) {
                total += harga
                totalBeli.text = NumberFormat(total.toString())
            }
            override fun onArrayItemClick(
                kode: ArrayList<String>,
                Arr_harga: HashMap<String, Int>,
                nama: ArrayList<String>,
                jenis: HashMap<String, String>,
                arr_jmlh: HashMap<String, Int>,
                arr_stok: HashMap<String, Int>
            ) {
                arr_kode = kode
                arr_nama = nama
                for (i in arr_nama) {
                    arr_harga[i] = Arr_harga[i]!!
                    arr_jumlah[i] = arr_jmlh[i]!!
                    arr_jenis[i] = jenis[i]!!
                    arr_stok2[i] = arr_stok[i]!!
                    bundle.putInt(i, arr_jumlah.get(i)!!)
                }
                log = Intent(this@MainActivity, Pembayaran::class.java)
                log.putExtra("key_kode", arr_kode)
                log.putExtra("key_nama", arr_nama)
                log.putExtra("key_jenis", arr_jenis)
                //log.putExtra("key_jumlah", arr_jumlah)
                log.putExtra("key_harga", arr_harga)
                log.putExtra("key_stok", arr_stok2)
                if(arr_jumlah.isEmpty() && !bundle.isEmpty){
                    log.putExtra("key_jumlah", bundle)
                }else{
                    log.putExtra("key_jumlah", arr_jumlah)
                }

                if (arr_kode.isEmpty()) {
                    log.putExtra("key_uang", "Rp.0")
                } else {
                    log.putExtra("key_uang", total)
                }

                if (arr_kode.isNotEmpty()) {
                    btn_bayar.setOnClickListener {
                        startActivity(log)
                    }
                }

            }
        })
        recyclerview.adapter = adapter
        return adapter
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {

        outState.putBundle("LIST_JUMLAH",bundle)
        outState.putBundle("LIST_STOK",bundle1)
        //outState.putBundle("LIST_JUMLAH",bundle)

        outState.putInt("Key_Harga_Save", total)
        super.onSaveInstanceState(outState, outPersistentState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        bundle = savedInstanceState.getBundle("LIST_JUMLAH")!!
        bundle1 = savedInstanceState.getBundle("LIST_STOK")!!

    }


    override fun onResume() {
        super.onResume()
        adapter = getAdapter2()
        adapter.notifyDataSetChanged()
        recyclerview.adapter = adapter
        //adapter to load all data jumlah
        Toast.makeText(this, bundle.toString(),Toast.LENGTH_LONG).show()
    }

    fun NumberFormat(s: String): String {
        var current: String = ""
        var parsed: Double
        var cleanString: String = s.replace("""[,.]""".toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted: String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<model_barang> = ArrayList()
        val kembali: ImageView= findViewById(R.id.kembali)

        // running a for loop to compare elements.
        for (item in listitem) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.nama.lowercase()
                    .contains(text.lowercase()) || item.kode.contains(text) || item.jenis.contains(
                    text.lowercase()
                )
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist)
        }
        kembali.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}