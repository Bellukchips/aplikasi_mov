package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.belluk.movapps.R
import com.belluk.movapps.adapter.PlaysAdapter
import com.belluk.movapps.model.Film
import com.belluk.movapps.model.Plays
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.img_detail
import kotlinx.android.synthetic.main.activity_detail_coming_soon.*
import kotlinx.android.synthetic.main.detail_item.*

class DetailComingSoonActivity : AppCompatActivity() {
    lateinit var mDatabaseComingSoon: DatabaseReference
    var dataListComingSoon = ArrayList<Plays>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_coming_soon)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val data =intent.getParcelableExtra<Film>("data")
        txt_title_detail.text = data.title
        txt_genre_detail.text = data.genre
        story_board.text = data.desc
        txt_rate.text =data.rating
        collapsing_toolbarComing.title = data.title
        collapsing_toolbarComing.setCollapsedTitleTextColor(resources.getColor(R.color.colorPrimary))
        collapsing_toolbarComing.setExpandedTitleColor(resources.getColor(R.color.colorWhite))
        Glide.with(this).load(data.poster).into(img_detail)
        mDatabaseComingSoon = FirebaseDatabase.getInstance().getReference("ComingSoon").child(data.title.toString()).child("play")
        who_played_recyle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        btn_pilih_bangkuComing.setOnClickListener {
            val intent = Intent(this,SelectCinema::class.java).putExtra("data",data)
            startActivity(intent)
        }
        getDataComingSoon()
    }
    private fun getDataComingSoon(): Boolean {
        mDatabaseComingSoon.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailComingSoonActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                dataListComingSoon.clear()
                for (getDetailComingSoon in dataSnapShot.children){
                    val film = getDetailComingSoon.getValue(Plays::class.java)
                    dataListComingSoon.add(film!!)
                }
                who_played_recyle.adapter = PlaysAdapter(dataListComingSoon){}
            }

        })
        return true
    }
}
