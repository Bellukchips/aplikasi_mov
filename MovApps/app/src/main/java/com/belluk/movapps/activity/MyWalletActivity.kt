package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.belluk.movapps.R
import com.belluk.movapps.adapter.WalletAdapter
import com.belluk.movapps.model.Wallet
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_wallet.*
import kotlinx.android.synthetic.main.activity_register.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MyWalletActivity : AppCompatActivity() {
    lateinit var preferencesUsers: PreferencesUsers
    lateinit var db:DatabaseReference
     var dataList=ArrayList<Wallet>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers  = PreferencesUsers(this)
        db = FirebaseDatabase.getInstance().getReference("Wallet").child(preferencesUsers.getValues("user").toString())
        if (preferencesUsers.getValues("saldo")?.isEmpty()!!){
            balance_wallet?.text = "Rp0"
        }else{
            balance_wallet?.text = preferencesUsers.getValues("saldo")
            currecy(preferencesUsers.getValues("saldo")!!.toDouble(),balance_wallet)
        }
        btn_top_up.setOnClickListener {
            startActivity(Intent(this,TopUpActivity::class.java))
        }
        rv_transaksi.layoutManager = LinearLayoutManager(this)
        progressbar_Wallet.visibility = View.VISIBLE
        db.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {
                dataList.clear()
                for (get in data.children){
                    val wallet = get.getValue(Wallet::class.java)
                    dataList.add(wallet!!)
                }
                if (dataList.isNotEmpty()){
                    progressbar_Wallet.visibility = View.GONE
                    empty_data.visibility = View.GONE
                    rv_transaksi.adapter = WalletAdapter(dataList){}
                }else{
                    empty_data.visibility = View.VISIBLE
                    progressbar_Wallet.visibility = View.GONE
                }

            }

        })
    }
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.text = formatRupiah.format(harga as Double)

    }
}
