package com.belluk.movapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.Film
import com.bumptech.glide.Glide

class NowPlayingAdapter(private var data:List<Film>,private val listenr:(Film)->Unit):
    RecyclerView.Adapter<NowPlayingAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter : Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflateView : View = layoutInflater.inflate(R.layout.row_item_now_playing,parent,false)
        return LeagueViewHolder(inflateView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listenr, ContextAdapter, position)
    }

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tv_kursi)
        private val tvGenre: TextView = view.findViewById(R.id.tv_genre_tiket)
        private val tvRate: TextView = view.findViewById(R.id.tv_rate_tiket_detail)

        private val tvImage: ImageView = view.findViewById(R.id.iv_poster_image_tiket)
        fun bindItem(data:Film, listenr: (Film) -> Unit, context: Context, position: Int){
            tvTitle.text = data.title
            tvGenre.text = data.genre
            tvRate.text = data.rating

            Glide.with(context).load(data.poster).into(tvImage)
            itemView.setOnClickListener {
                listenr(data)
            }
        }
    }
}