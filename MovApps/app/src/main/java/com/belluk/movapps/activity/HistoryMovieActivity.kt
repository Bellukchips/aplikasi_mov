package com.belluk.movapps.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.adapter.HistoryAdapter
import com.belluk.movapps.db.MovieHelper
import com.belluk.movapps.mapping.MappingHelper
import com.belluk.movapps.model.History
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_history_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HistoryMovieActivity : AppCompatActivity() {
    private lateinit var adapter:HistoryAdapter
    private lateinit var movieHelper: MovieHelper
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_movie)
        val rc = findViewById<RecyclerView>(R.id.rc_history)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        rc.layoutManager  = LinearLayoutManager(this)
        rc.setHasFixedSize(true)
        adapter = HistoryAdapter()
        movieHelper = MovieHelper.getInstance(applicationContext)
        if (savedInstanceState == null){
            loadData()
        }else{
            val list = savedInstanceState.getParcelableArrayList<History>(EXTRA_STATE)
            if (list != null){
                adapter.listTiket = list
            }
        }
        rc.adapter = adapter
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main){
            progress_history.visibility = View.VISIBLE
            val def = async(Dispatchers.IO) {
                val cursor  = movieHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progress_history.visibility = View.GONE
            val movie = def.await()
            if (movie.size > 0){
                adapter.listTiket = movie
            }else{
                adapter.listTiket = ArrayList()
                Toast.makeText(this@HistoryMovieActivity,"Empty Data",Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onStart() {
        val movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()
        super.onStart()
    }
    override fun onDestroy() {
        val movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.close()
        super.onDestroy()
    }
}
