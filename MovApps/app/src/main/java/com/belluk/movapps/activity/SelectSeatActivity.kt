package com.belluk.movapps.activity

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.belluk.movapps.R
import com.belluk.movapps.adapter.BioskopAdapter
import com.belluk.movapps.adapter.SelectSeatAdapter
import com.belluk.movapps.db.MovieHelper
import com.belluk.movapps.fragment.NoConnectionFragment
import com.belluk.movapps.model.Bioskop
import com.belluk.movapps.model.Checkout
import com.belluk.movapps.model.Film
import com.belluk.movapps.model.Seat
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_pilih_bangku.*
import kotlinx.android.synthetic.main.activity_select_cinema.*
import kotlinx.android.synthetic.main.row_item_seat.*

class SelectSeatActivity : AppCompatActivity() {
    lateinit var builder : AlertDialog.Builder
    private var dataList = ArrayList<Checkout>()
    lateinit var mDatabaseA:DatabaseReference
    private var dataSeatA = ArrayList<Seat>()
    private var dataSeatB = ArrayList<Seat>()
    private var dataSeatC = ArrayList<Seat>()
    var check:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_bangku)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val data =intent.getParcelableExtra<Film>("data")
        val dataBioskop = intent.getParcelableExtra<Bioskop>("bioskop")
        mDatabaseA = FirebaseDatabase.getInstance().getReference("Seat")
        rc_seat_A.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rc_seat_B.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rc_seat_C.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        title_movie_select_seat.text = data.title
        btn_next_bangku.setOnClickListener {
            builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.calendar,null)
            val close = dialogView.findViewById<Button>(R.id.btn_close_calendar)
            val calendar = dialogView.findViewById<CalendarView>(R.id.calendarView)
            builder.setView(dialogView)
            builder.setCancelable(true)
            val dialog = builder.create()
            close.setOnClickListener {
                dialog.dismiss()
            }
                calendar.setOnDateChangeListener { calendarView, year, month, day ->
                    val date = ""+day+"/"+(month+1)+"/"+year
                    val intent = Intent(this,CheckOutActivity::class.java).putExtra("data",dataList).putExtra("datas",data).putExtra("bioskop",dataBioskop).putExtra("date",date)
                    startActivity(intent)
                    dialog.dismiss()
                    finishAffinity()
                }
            dialog.show()
        }
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        getData()
        showLoading(true)
    }
    private var broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val connectF = NoConnectionFragment()

            val notConnect = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false)
            if (notConnect){
                layout_frame_seat.visibility = View.VISIBLE
                showLoading(false)
                rc_seat_A.visibility = View.GONE
                rc_seat_B.visibility = View.GONE
                rc_seat_C.visibility = View.GONE
                btn_next_bangku.visibility = View.INVISIBLE
                setFragment(connectF)
            }else{
                showLoading(true)
                btn_next_bangku.visibility = View.GONE
                layout_frame_seat.visibility = View.GONE
                rc_seat_A.visibility =  View.VISIBLE
                rc_seat_B.visibility =  View.VISIBLE
                rc_seat_C.visibility = View.VISIBLE
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
    private fun setFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame_seat,fragment)
        fragmentTransaction.commit()
    }
    private fun getData() {
        val dataBioskop = intent.getParcelableExtra<Bioskop>("bioskop")
        val data =intent.getParcelableExtra<Film>("data")
        mDatabaseA.child(dataBioskop.nama.toString()).child(data.cinema.toString()).child("seat").child("A").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SelectSeatActivity,""+error.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnap: DataSnapshot) {
                dataSeatA.clear()
                for (getData in dataSnap.children){
                    val seat = getData.getValue(Seat::class.java)
                    dataSeatA.add(seat!!)
                }
                if (dataSeatA.isNotEmpty()){
                    showLoading(false)
                    rc_seat_A.adapter = SelectSeatAdapter(dataSeatA){
                        if (it.checked == true){
                            val total = dataList.size - 1
                            dataList.remove(Checkout(it.nama,dataBioskop.price,"1","A",data.cinema,dataBioskop.nama))
                            btn_next_bangku.text = "Beli Tiket("+total+")"
                            if (dataList.size < 1){
                                btn_next_bangku.visibility = View.INVISIBLE
                            }
                        }else if (it.checked == false){
                            val total = dataList.size + 1
                            btn_next_bangku.visibility = View.VISIBLE
                            btn_next_bangku.text = "Beli Tiket("+total+")"
                            dataList.add(Checkout(it.nama,dataBioskop.price,"1","A",data.cinema,dataBioskop.nama))
                        }
                    }
                }else{
                    showLoading(true)
                }
            }

        })
        mDatabaseA.child(dataBioskop.nama.toString()).child(data.cinema.toString()).child("seat").child("B").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SelectSeatActivity,""+error.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnap: DataSnapshot) {
                dataSeatB.clear()
                for (getData in dataSnap.children){
                    val seat = getData.getValue(Seat::class.java)
                    dataSeatB.add(seat!!)
                }
                if (dataSeatB.isNotEmpty()){
                    showLoading(false)
                    rc_seat_B.adapter = SelectSeatAdapter(dataSeatB){
                        if (it.checked == true){
                            val total = dataList.size - 1
                            dataList.remove(Checkout(it.nama,dataBioskop.price,"1","B",data.cinema,dataBioskop.nama))
                            btn_next_bangku.text = "Beli Tiket("+total+")"
                            if (dataList.size < 1){
                                btn_next_bangku.visibility = View.INVISIBLE
                            }
                        }else if (it.checked == false){
                            val total = dataList.size + 1
                            btn_next_bangku.visibility = View.VISIBLE
                            btn_next_bangku.text = "Beli Tiket("+total+")"
                            dataList.add(Checkout(it.nama,dataBioskop.price,"1","B",data.cinema,dataBioskop.nama))
                        }

                    }
                }else{
                    showLoading(true)
                }
            }

        })
        mDatabaseA.child(dataBioskop.nama.toString()).child(data.cinema.toString()).child("seat").child("C").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SelectSeatActivity,""+error.message,Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnap: DataSnapshot) {
                dataSeatC.clear()
                for (getData in dataSnap.children){
                    val seat = getData.getValue(Seat::class.java)
                    dataSeatC.add(seat!!)
                }
                if (dataSeatC.isNotEmpty()){
                    rc_seat_C.adapter = SelectSeatAdapter(dataSeatC){
                        if (it.checked == true){
                            val total = dataList.size - 1
                            dataList.remove(Checkout(it.nama,dataBioskop.price,"1","C",data.cinema,dataBioskop.nama))
                            btn_next_bangku.text = "Beli Tiket("+total+")"
                            if (dataList.size < 1){
                                btn_next_bangku.visibility = View.INVISIBLE
                            }

                        }else if (it.checked == false){
                            val total = dataList.size + 1
                            btn_next_bangku.visibility = View.VISIBLE
                            btn_next_bangku.text = "Beli Tiket("+total+")"
                            dataList.add(Checkout(it.nama,dataBioskop.price,"1","C",data.cinema,dataBioskop.nama))
                        }
                    }
                }else{
                    showLoading(true)
                }
            }

        })
    }
    private fun showLoading(loading:Boolean){
        if (loading == true){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }

}

