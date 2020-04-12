package com.belluk.movapps.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.belluk.movapps.R
import com.belluk.movapps.adapter.BioskopAdapter
import com.belluk.movapps.fragment.NoConnectionFragment
import com.belluk.movapps.model.Bioskop
import com.belluk.movapps.model.Film
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_select_cinema.*

class SelectCinema : AppCompatActivity() {
    private var dataList = ArrayList<Bioskop>()
    private var dataListA = ArrayList<Bioskop>()
    lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_cinema)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Bioskop")
        rc_bioskop.layoutManager = LinearLayoutManager(this)
        getData()
        btn_cancel.setOnClickListener {
            val intent = Intent(this,HomeScreenActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        showLoading(true)
        search_cinema.filters = arrayOf<InputFilter>(AllCaps())
        search_cinema.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                val data = intent.getParcelableExtra<Film>("data")
                val a =   mDatabaseReference.orderByChild("nama").equalTo(s.toString())
                a.addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        dataListA.clear()
                        for (b in p0.children){
                            val data = b.getValue(Bioskop::class.java)
                            dataListA.add(data!!)
                        }
                        if (dataListA.isNotEmpty()){
                            showLoading(false)
                                rc_bioskop.visibility = View.VISIBLE
                                rc_bioskop.adapter = BioskopAdapter(dataListA){
                                    val intent = Intent(this@SelectCinema,SelectSeatActivity::class.java).putExtra("data",data).putExtra("bioskop",it)
                                    startActivity(intent)
                                }
                        }else{
                            getData()
                        }

                    }

                })
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
    private var broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val connectF = NoConnectionFragment()

            val notConnect = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false)
            if (notConnect){
                layout_frame_cinema.visibility = View.VISIBLE
               showLoading(false)
                rc_bioskop.visibility  = View.GONE
                setFragment(connectF)
            }else{
                layout_frame_cinema.visibility = View.GONE
                showLoading(true)
                rc_bioskop.visibility  = View.VISIBLE
                getData()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
    private fun getData() {
        val data = intent.getParcelableExtra<Film>("data")
        mDatabaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SelectCinema,""+error.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnap: DataSnapshot) {
                dataList.clear()
                for (getData in dataSnap.children){
                    val bioskop = getData.getValue(Bioskop::class.java)
                    dataList.add(bioskop!!)
                }
                if (dataList.isNotEmpty()){
                    showLoading(false)
                    rc_bioskop.adapter = BioskopAdapter(dataList){
                        val intent = Intent(this@SelectCinema,SelectSeatActivity::class.java).putExtra("data",data).putExtra("bioskop",it)
                        startActivity(intent)
                    }
                }else{
                    showLoading(true)
                }
            }

        })
    }
    private fun setFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame_cinema,fragment)
        fragmentTransaction.commit()
    }
    private fun showLoading(loading:Boolean){
        if (loading){
            progressBarCinema.visibility = View.VISIBLE
        }else{
            progressBarCinema.visibility = View.GONE
        }
    }
}

