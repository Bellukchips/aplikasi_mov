package com.belluk.movapps.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.belluk.movapps.R
import com.belluk.movapps.activity.DetailNowPlayingActivity
import com.belluk.movapps.activity.DetailComingSoonActivity
import com.belluk.movapps.adapter.ComingSoonAdapter
import com.belluk.movapps.adapter.NowPlayingAdapter
import com.belluk.movapps.model.Film
import com.belluk.movapps.utils.PreferencesUsers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {
    private lateinit var preferencesUsers: PreferencesUsers
    lateinit var mDatabaseNowPlaying: DatabaseReference
    lateinit var dbUser:DatabaseReference
    lateinit var mDatabaseComingSoon:DatabaseReference
    private var dataListSoon = ArrayList<Film>()
    private var dataList = ArrayList<Film>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_dashboard, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesUsers = PreferencesUsers(activity!!.applicationContext)
        mDatabaseNowPlaying = FirebaseDatabase.getInstance().getReference("NowPlaying")
        mDatabaseComingSoon = FirebaseDatabase.getInstance().getReference("ComingSoon")
        dbUser = FirebaseDatabase.getInstance().getReference("User").child(preferencesUsers.getValues("user").toString())
        val tv_name = view?.findViewById<TextView>(R.id.txt_show_name_homeScreen)
        val tv_balance = view?.findViewById<TextView>(R.id.txt_balance_wallet_homeScreen)
        val rc_nowPlaying = view?.findViewById<RecyclerView>(R.id.now_playing_recyle)
        val rc_comingSoon = view?.findViewById<RecyclerView>(R.id.coming_soon_recyle)
        val progressBarNow = view?.findViewById<ProgressBar>(R.id.progressBar_nowPlaying)
        val progressBarComing = view?.findViewById<ProgressBar>(R.id.progressBarComing)
        tv_name?.text = preferencesUsers.getValues("nama")
        if (preferencesUsers.getValues("saldo")?.isEmpty()!!){
            tv_balance?.text = "Rp0"
        }else{
            tv_balance?.text = preferencesUsers.getValues("saldo")
            currecy(preferencesUsers.getValues("saldo")!!.toDouble(),txt_balance_wallet_homeScreen)
        }
        if (preferencesUsers.getValues("url")?.isEmpty()!!){
            img_user_home.setImageResource(R.drawable.user_pic)
        }else{
            Glide.with(this).load(preferencesUsers.getValues("url"))
                .apply(RequestOptions.circleCropTransform()).into(img_user_home)
        }

        Log.v("tamvan", "url "+preferencesUsers.getValues("url"))
        rc_nowPlaying?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rc_comingSoon?.layoutManager = LinearLayoutManager(context!!.applicationContext)
        getData()
        progressBarComing?.visibility = View.VISIBLE
        progressBarNow?.visibility = View.VISIBLE

    }
    private fun getData() {
        val rc_comingSoon = view?.findViewById<RecyclerView>(R.id.coming_soon_recyle)
        val rc_nowPlaying = view?.findViewById<RecyclerView>(R.id.now_playing_recyle)
        val progressBarNow = view?.findViewById<ProgressBar>(R.id.progressBar_nowPlaying)
        val progressBarComing = view?.findViewById<ProgressBar>(R.id.progressBarComing)
        mDatabaseNowPlaying.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnap: DataSnapshot) {
                dataList.clear()
                for(getDataSnapshoot in dataSnap.children){
                    val film = getDataSnapshoot.getValue(Film::class.java)
                    dataList.add(film!!)
                }
                if (dataList.isNotEmpty()){
                    progressBarNow?.visibility = View.GONE
                    rc_nowPlaying?.adapter = NowPlayingAdapter(dataList){
                        val intent = Intent(context,DetailNowPlayingActivity::class.java).putExtra("data",it)
                        startActivity(intent)
                    }
                }else{
                    progressBarNow?.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
        mDatabaseComingSoon.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnap: DataSnapshot) {
                dataListSoon.clear()
                for (getData in dataSnap.children){
                    val comingSoon = getData.getValue(Film::class.java)
                    dataListSoon.add(comingSoon!!)
                }
                if (dataListSoon.isNotEmpty()){
                    progressBarComing?.visibility = View.GONE
                    rc_comingSoon?.adapter = ComingSoonAdapter(dataListSoon){
                        val intent = Intent(context,DetailComingSoonActivity::class.java).putExtra("data",it)
                        startActivity(intent)
                    }
                }else{
                    progressBarComing?.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,""+error.message,Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.text = formatRupiah.format(harga as Double)

    }


}
