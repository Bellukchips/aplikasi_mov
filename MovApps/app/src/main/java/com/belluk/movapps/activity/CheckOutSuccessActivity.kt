package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.belluk.movapps.R
import com.belluk.movapps.model.*
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_check_out_success.*

class CheckOutSuccessActivity : AppCompatActivity() {
    private var dataList = ArrayList<Checkout>()
    lateinit var preferencesUsers: PreferencesUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out_success)
        preferencesUsers = PreferencesUsers(this)
        val data = intent.getParcelableExtra<Checkout>("tiket")
        val film = intent.getParcelableExtra<Film>("film")
        val tiket = intent.getParcelableExtra<Tiket>("mytiket")
        val dataBioskop = intent.getParcelableExtra<Bioskop>("bioskop")
        dataList = intent.getSerializableExtra("tiket") as ArrayList<Checkout>
        val dateGet = intent.getStringExtra("date")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        btn_home.setOnClickListener {
            val intent = Intent(this,HomeScreenActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        btn_lihat_tiket.setOnClickListener {
            val intent = Intent(this,TiketActivity::class.java).putExtra("tiket",dataList).putExtra("tikets",data).putExtra("tiketss",film).putExtra("bioskop",dataBioskop).putExtra("mytiket",tiket).putExtra("date",dateGet)
            startActivity(intent)
        }

    }
}
