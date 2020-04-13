package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.belluk.movapps.R
import com.belluk.movapps.utils.PreferencesUsers
import kotlinx.android.synthetic.main.activity_my_wallet.*
import kotlinx.android.synthetic.main.activity_register.*
import java.text.NumberFormat
import java.util.*

class MyWalletActivity : AppCompatActivity() {
    lateinit var preferencesUsers: PreferencesUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers  = PreferencesUsers(this)
        if (preferencesUsers.getValues("saldo")?.isEmpty()!!){
            balance_wallet?.text = "Rp0"
        }else{
            balance_wallet?.text = preferencesUsers.getValues("saldo")
            currecy(preferencesUsers.getValues("saldo")!!.toDouble(),balance_wallet)
        }
        btn_top_up.setOnClickListener {
            startActivity(Intent(this,TopUpActivity::class.java))
        }
    }
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.text = formatRupiah.format(harga as Double)

    }
}
