package com.belluk.movapps.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.belluk.movapps.R
import com.belluk.movapps.model.Wallet
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_top_up.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TopUpActivity : AppCompatActivity() {
    var status:Boolean = false
    var status1:Boolean = false
    var status2:Boolean = false
    var status3:Boolean = false
    var status4:Boolean = false
    var status5:Boolean = false
    lateinit var iSaldo:String
    lateinit var mdb:DatabaseReference
    lateinit var dbWallet:DatabaseReference
    private var dateFormat: SimpleDateFormat? = null
    private var calendar: Calendar? = null
    lateinit var preferencesUsers: PreferencesUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        mdb = FirebaseDatabase.getInstance().getReference("User").child(preferencesUsers.getValues("user").toString()).child("saldo")
        dbWallet = FirebaseDatabase.getInstance().getReference("Wallet")
        dateFormat =  SimpleDateFormat("MM/dd/yyyy")
        calendar = Calendar.getInstance()
        if (preferencesUsers.getValues("saldo")?.isEmpty()!!){
            balance_topup?.text = "Rp0"
        }else{
            balance_topup?.text = preferencesUsers.getValues("saldo")
            currecy(preferencesUsers.getValues("saldo")!!.toDouble(),balance_topup)
        }
        txt_amount.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                arg0: CharSequence, arg1: Int, arg2: Int,
                arg3: Int
            ) {
            }

            override fun beforeTextChanged(
                arg0: CharSequence, arg1: Int,
                arg2: Int, arg3: Int
            ) {
            }

            override fun afterTextChanged(name: Editable) {
                if (txt_amount.text.toString().isNotEmpty()) {
                    showButton(true)
                }
            }
        })
        btn_top_up.setOnClickListener {
            val random = GenerateString.randomString(7)
            val date = dateFormat!!.format(Date())
            val saldo = preferencesUsers.getValues("saldo")!!.toDouble()
           if (status == true){
               val topup = 10000.0
               val add = saldo + topup
               val wallet = Wallet(preferencesUsers.getValues("user"),random,"Top Up",date,"10000","1")
               preferencesUsers.setValues("saldo",add.toString())
               mdb.setValue(add.toString())
               dbWallet.child(preferencesUsers.getValues("user").toString()).child(random).setValue(wallet)
               startActivity(Intent(this,TopUpSuccess::class.java))
           }else if (status1 ==  true){
               val topup = 20000.0
               val add = saldo + topup
               val wallet = Wallet(preferencesUsers.getValues("user"),random,"Top Up",date,"20000","1")
               preferencesUsers.setValues("saldo",add.toString())
               mdb.setValue(add.toString())
               dbWallet.child(preferencesUsers.getValues("user").toString()).child(random).setValue(wallet)
               startActivity(Intent(this,TopUpSuccess::class.java))
           }else if (status2 == true){
               val topup = 30000.0
               val add = saldo + topup
               val wallet = Wallet(preferencesUsers.getValues("user"),random,"Top Up",date,"30000","1")
               preferencesUsers.setValues("saldo",add.toString())
               mdb.setValue(add.toString())
               dbWallet.child(preferencesUsers.getValues("user").toString()).child(random).setValue(wallet)
               startActivity(Intent(this,TopUpSuccess::class.java))
           }else if (status3 == true) {
               val topup = 40000.0
               val add = saldo + topup
               val wallet = Wallet(preferencesUsers.getValues("user"),random,"Top Up",date,"40000","1")
               preferencesUsers.setValues("saldo", add.toString())
               mdb.setValue(add.toString())
               dbWallet.child(preferencesUsers.getValues("user").toString()).child(random).setValue(wallet)
               startActivity(Intent(this,TopUpSuccess::class.java))
           }else if(status4 ==  true){
               val topup = 50000.0
               val add = saldo + topup
               val wallet = Wallet(preferencesUsers.getValues("user"),random,"Top Up",date,"50000","1")
               preferencesUsers.setValues("saldo",add.toString())
               mdb.setValue(add.toString())
               dbWallet.child(preferencesUsers.getValues("user").toString()).child(random).setValue(wallet)
               startActivity(Intent(this,TopUpSuccess::class.java))
           }else if (status5 == true){
               val topup = 100000.0
               val add = saldo + topup
               val wallet = Wallet(preferencesUsers.getValues("user"),random,"Top Up",date,"100000","1")
               preferencesUsers.setValues("saldo",add.toString())
               mdb.setValue(add.toString())
               dbWallet.child(preferencesUsers.getValues("user").toString()).child(random).setValue(wallet)
               startActivity(Intent(this,TopUpSuccess::class.java))
           }else{
               iSaldo = txt_amount.text.toString()
               if (iSaldo.equals("")) {
                   txt_amount.error = "Isi Saldo Di sini"
                   txt_amount.requestFocus()
               }else{
                   val mysaldo = iSaldo.toDouble()
                   val add = saldo + mysaldo
                   val wallet = Wallet(preferencesUsers.getValues("user"),random,"Top Up",date,mysaldo.toString(),"1")
                   preferencesUsers.setValues("saldo",add.toString())
                   mdb.setValue(add.toString())
                   dbWallet.child(preferencesUsers.getValues("user").toString()).child(random).setValue(wallet)
                   startActivity(Intent(this,TopUpSuccess::class.java))
               }
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
                txt_amount.setText("")
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
                txt_amount.setText("")
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
                txt_amount.setText("")
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
                txt_amount.setText("")
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
                txt_amount.setText("")
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
        showButton(true)
        txt_amount.isEnabled = false
        txt_amount.setText("")
        txt_amount.error = null
    }
    private fun deselectMoney(textView:TextView){
        textView.setTextColor(resources.getColor(R.color.colorWhite))
        textView.setBackgroundResource(R.drawable.shape_line_white)
        showButton(false)
        txt_amount.isEnabled = true
    }
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.text = formatRupiah.format(harga as Double)

    }
    private fun showButton(status:Boolean){
        if (status){
            btn_top_up.visibility = View.VISIBLE
        }else{
            btn_top_up.visibility = View.INVISIBLE
        }
    }
    object GenerateString {
        /**
         * Genera una password RANDOM
         */
        const val DATA =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        var RANDOM = Random()
        fun randomString(len: Int): String {
            val sb = StringBuilder(len)
            for (i in 0 until len) {
                sb.append(DATA[RANDOM.nextInt(DATA.length)])
            }
            return sb.toString()
        }
    }

}
