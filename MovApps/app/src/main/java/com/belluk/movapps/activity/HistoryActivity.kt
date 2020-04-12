package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.adapter.MyTiketAdapter
import com.belluk.movapps.adapter.TiketAdapter
import com.belluk.movapps.model.Tiket
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history_movie.*
import kotlinx.android.synthetic.main.activity_register.*

class HistoryActivity : AppCompatActivity() {
    lateinit var mdb:DatabaseReference
    lateinit var preferencesUsers: PreferencesUsers
    var datalist = ArrayList<Tiket>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val progressbar = findViewById<ProgressBar>(R.id.progressbar_history)
        val rc = findViewById<RecyclerView>(R.id.recycler_history)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        mdb = FirebaseDatabase.getInstance().getReference("History").child(preferencesUsers.getValues("user").toString())
        rc.layoutManager = LinearLayoutManager(this)
        progressbar.visibility = View.VISIBLE
        mdb.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("history",p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                datalist.clear()
                for (i in p0.children){
                    val history = i.getValue(Tiket::class.java)
                    datalist.add(history!!)
                }
                if (datalist.isNotEmpty()){
                    progressbar.visibility = View.GONE
                    logo_empty_history.visibility = View.GONE
                    tv_empty.visibility = View.GONE
                    rc.adapter = MyTiketAdapter(datalist){}
                }else{
                    logo_empty_history.visibility = View.VISIBLE
                    tv_empty.visibility = View.VISIBLE
                    progressbar.visibility = View.GONE
                }
            }

        })
    }
}
