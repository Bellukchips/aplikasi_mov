package com.belluk.movapps.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.belluk.movapps.R
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_top_up.*
import java.text.NumberFormat
import java.util.*

class TopUpActivity : AppCompatActivity() {
    var status:Boolean = false
    var status1:Boolean = false
    var status2:Boolean = false
    var status3:Boolean = false
    var status4:Boolean = false
    var status5:Boolean = false
    var topup:Double = 0.0
    lateinit var mdb:DatabaseReference
    lateinit var preferencesUsers: PreferencesUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        mdb = FirebaseDatabase.getInstance().getReference("User").child(preferencesUsers.getValues("user").toString()).child("saldo")
        if (preferencesUsers.getValues("saldo")?.isEmpty()!!){
            balance_topup?.text = "Rp0"
        }else{
            balance_topup?.text = preferencesUsers.getValues("saldo")
            currecy(preferencesUsers.getValues("saldo")!!.toDouble(),balance_topup)
        }
        btn_top_up.setOnClickListener {
            val saldo = preferencesUsers.getValues("saldo")!!.toDouble()
            if (status == true){
                topup = 10000.0
                val hitung = saldo + topup
                preferencesUsers.setValues("saldo", hitung.toString())
                mdb.setValue(hitung.toString())
            }else if (status1 == true){
                mdb.setValue("20000")
            }
        }
        tv_10k.setOnClickListener {
            if (status){
                deselectMoney(tv_10k)
                status = false
                tv_20k.isClickable = true
                tv_30k.isClickable = true
                tv_40k.isClickable = true
                tv_50k.isClickable = true
                tv_100k.isClickable = true
            }else{
                selectMoney(tv_10k)
                status = true
                tv_20k.isClickable = false
                tv_30k.isClickable = false
                tv_40k.isClickable = false
                tv_50k.isClickable = false
                tv_100k.isClickable = false
            }
        }
        tv_20k.setOnClickListener {
            if (status1){
                deselectMoney(tv_20k)
                status1 = false
                tv_10k.isClickable =true
                tv_30k.isClickable = true
                tv_40k.isClickable = true
                tv_50k.isClickable = true
                tv_100k.isClickable = true
            }else{
                selectMoney(tv_20k)
                status1 = true
                tv_10k.isClickable =false
                tv_30k.isClickable = false
                tv_40k.isClickable = false
                tv_50k.isClickable = false
                tv_100k.isClickable = false
            }
        }
        tv_30k.setOnClickListener {
            if (status2){
                deselectMoney(tv_30k)
                status2 = false
                tv_10k.isClickable =true
                tv_20k.isClickable = true
                tv_40k.isClickable = true
                tv_50k.isClickable = true
                tv_100k.isClickable = true
            }else{
                selectMoney(tv_30k)
                status2 = true
                tv_10k.isClickable =false
                tv_20k.isClickable = false
                tv_40k.isClickable = false
                tv_50k.isClickable = false
                tv_100k.isClickable = false
            }
        }
        tv_40k.setOnClickListener {
            if (status3){
                deselectMoney(tv_40k)
                status3 = false
                tv_10k.isClickable =true
                tv_20k.isClickable = true
                tv_30k.isClickable = true
                tv_50k.isClickable = true
                tv_100k.isClickable = true
            }else{
                selectMoney(tv_40k)
                status3 = true
                tv_10k.isClickable =false
                tv_20k.isClickable = false
                tv_30k.isClickable = false
                tv_50k.isClickable = false
                tv_100k.isClickable = false
            }
        }
        tv_50k.setOnClickListener {
            if (status4){
                deselectMoney(tv_50k)
                status4 = false
                tv_10k.isClickable =true
                tv_20k.isClickable = true
                tv_30k.isClickable = true
                tv_40k.isClickable = true
                tv_100k.isClickable = true
            }else{
                selectMoney(tv_50k)
                status4 = true
                tv_10k.isClickable =false
                tv_20k.isClickable = false
                tv_30k.isClickable = false
                tv_40k.isClickable = false
                tv_100k.isClickable = false
            }
        }
        tv_100k.setOnClickListener {
            if (status5){
                deselectMoney(tv_100k)
                status5 = false
                tv_10k.isClickable =true
                tv_20k.isClickable = true
                tv_30k.isClickable = true
                tv_40k.isClickable = true
                tv_50k.isClickable = true
            }else{
                selectMoney(tv_100k)
                status5 = true
                tv_10k.isClickable =false
                tv_20k.isClickable = false
                tv_30k.isClickable = false
                tv_40k.isClickable = false
                tv_50k.isClickable = false
            }
        }
    }

    private fun selectMoney(txt:TextView){
        txt.setTextColor(resources.getColor(R.color.colorRedAccent))
        txt.setBackgroundResource(R.drawable.shape_line_pink)
        btn_top_up.visibility = View.VISIBLE
    }
    private fun deselectMoney(textView:TextView){
        textView.setTextColor(resources.getColor(R.color.colorWhite))
        textView.setBackgroundResource(R.drawable.shape_line_white)

        btn_top_up.visibility = View.INVISIBLE
    }
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.text = formatRupiah.format(harga as Double)

    }
}
