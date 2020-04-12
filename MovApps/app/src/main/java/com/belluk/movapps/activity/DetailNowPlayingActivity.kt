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
import kotlinx.android.synthetic.main.detail_item.*

class DetailNowPlayingActivity : AppCompatActivity() {
    lateinit var mDatabaseNowPlaying:DatabaseReference
    var dataListNowPlaying = ArrayList<Plays>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val data =intent.getParcelableExtra<Film>("data")
        mDatabaseNowPlaying = FirebaseDatabase.getInstance().getReference("NowPlaying").child(data.title.toString()).child("play")
        txt_title_detail.text = data.title
        txt_genre_detail.text = data.genre
        story_board.text = data.desc
        txt_rate.text =data.rating
        collapsing_toolbar.title = data.title
        collapsing_toolbar.setCollapsedTitleTextColor(resources.getColor(R.color.colorPrimary))
        collapsing_toolbar.setExpandedTitleColor(resources.getColor(R.color.colorWhite))
        Glide.with(this).load(data.poster).into(img_detail)
        who_played_recyle.layoutManager =LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        btn_pilih_bangku.setOnClickListener {
            val intent = Intent(this,SelectCinema::class.java).putExtra("data",data)
            startActivity(intent)
        }
        getDataNowPlaying()

    }
    private fun getDataNowPlaying(): Boolean {
        mDatabaseNowPlaying.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailNowPlayingActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataListNowPlaying.clear()
                for (getDetail in dataSnapshot.children){
                    val film = getDetail.getValue(Plays::class.java)
                    dataListNowPlaying.add(film!!)

                }
                who_played_recyle.adapter = PlaysAdapter(dataListNowPlaying){}
            }

        })
        return true
    }
}
