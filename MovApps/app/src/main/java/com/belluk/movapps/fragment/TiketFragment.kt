package com.belluk.movapps.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.activity.DetailTiketActivity
import com.belluk.movapps.activity.HistoryActivity
import com.belluk.movapps.adapter.MyTiketAdapter
import com.belluk.movapps.db.MovieHelper
import com.belluk.movapps.model.History
import com.belluk.movapps.model.Seat
import com.belluk.movapps.model.Tiket
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class TiketFragment : Fragment() {
    private lateinit var preferencesUsers: PreferencesUsers
    var dataList = ArrayList<Tiket>()
    var dataSeat = ArrayList<Seat>()
    lateinit var mDatabase:DatabaseReference
    lateinit var mHistory:DatabaseReference
    private var dateFormat: SimpleDateFormat? = null
    lateinit var mdb:DatabaseReference
    private lateinit var history: History
    private lateinit var movieHelper: MovieHelper
    override fun onStart() {
//        val movieHelper = MovieHelper.getInstance(context!!.applicationContext)
//        movieHelper.open()
        super.onStart()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tiket, container, false)
        preferencesUsers = PreferencesUsers(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Tiket").child(preferencesUsers.getValues("user").toString())
        val tiket = view.findViewById(R.id.rc_tiket) as RecyclerView
        val total = view.findViewById(R.id.tv_total) as TextView
        val tv_empty = view.findViewById(R.id.tv_empty) as TextView
        val progressbar = view.findViewById(R.id.progressBarTiket) as ProgressBar
        val history = view.findViewById<ImageView>(R.id.history_icon)
        val empty = view.findViewById<ImageView>(R.id.img_empty)
        tiket.layoutManager = LinearLayoutManager(activity)
        progressbar.visibility = View.VISIBLE

//        movieHelper = MovieHelper.getInstance(context!!.applicationContext)
        dateFormat = SimpleDateFormat("d/M/yyyy")
        history.setOnClickListener {
            val intent = Intent(context,HistoryActivity::class.java)
            startActivity(intent)
        }
        mdb = FirebaseDatabase.getInstance().reference
        mHistory = FirebaseDatabase.getInstance().getReference("History")
        mDatabase.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,""+error.message,Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(dataSnap: DataSnapshot) {
                dataList.clear()
                for (i in dataSnap.children){
                    val data = i.getValue(Tiket::class.java)
                    dataList.add(data!!)
                }
                if (dataList.isNotEmpty()){
                    progressbar.visibility = View.GONE
                    tiket.adapter = MyTiketAdapter(dataList){
                        val intent = Intent(context,DetailTiketActivity::class.java).putExtra("data",it)
                        startActivity(intent)
                    }
                    for (i in dataList.indices){
                        val dateTiket = dataList[i].date
                        val user = dataList[i].user
                        val kode = dataList[i].kodeTiket
                        val nama = dataList[i].nama
                        val bioskop = dataList[i].bioskop
                        val cinema = dataList[i].cinema
                        val genre = dataList[i].genre
                        val poster = dataList[i].poster
                        val rating = dataList[i].rating
                        val timer = dataList[i].timer
                        val date = dateFormat!!.format(Date())
                        val date1 = dateFormat!!.parse(date)
                        val date2 = dateFormat!!.parse(dateTiket)
                        if (date1.after(date2)){
                            val dataTiket = Tiket(nama,bioskop,genre,user,dateTiket,poster,rating,timer,kode,cinema)
                            val seat = mdb.child("Tiket").child(user.toString()).child(kode.toString()).child("kursi")
                            val que = mdb.child("Tiket").child(user.toString()).orderByChild("kodeTiket").equalTo(kode.toString())
                            que.addValueEventListener(object :ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e(TAG, "onCancelled", p0.toException());
                                }
                                override fun onDataChange(p0: DataSnapshot) {
                                    for (appleSnapshot in p0.children) {
                                        appleSnapshot.ref.removeValue()
                                    }
                                }
                            })
                            seat.addValueEventListener(object :ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e(TAG, "onCancelled", p0.toException());
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    for (i in p0.children){
                                        val seat = i.getValue(Seat::class.java)
                                        dataSeat.add(seat!!)
                                    }
                                    mHistory.child(user.toString()).child(kode.toString()).setValue(dataTiket)
                                    for (a in dataSeat.indices){
                                        mdb.child("Seat").child(dataList[i].bioskop.toString()).child(dataList[i].cinema.toString()).child("seat").child(dataSeat[a].golSeat.toString()).child(dataSeat[a].nama.toString()).child("status").setValue("0")
                                    }
                                }

                            })
                        }


                    }

                    total.text = dataList.size.toString() + " Movies"
                }else{
                    progressbar.visibility = View.GONE
                    tiket.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                    tv_empty.visibility = View.VISIBLE
                }
            }

        })
        return view
    }

}
