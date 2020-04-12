package com.belluk.movapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.Film
import com.bumptech.glide.Glide

class ComingSoonAdapter(private var data:List<Film>,
                        private var listener:(Film)->Unit)
    :RecyclerView.Adapter<ComingSoonAdapter.LeagueViewHolder>(){
    lateinit var ContextAdapter : android.content.Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_coming_soon, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTitle: TextView = view.findViewById(R.id.tv_kursi)
        private val tvGenre: TextView = view.findViewById(R.id.tv_genre_tiket)
        private val tvRate: TextView = view.findViewById(R.id.tv_rate_tiket_detail)

        private val tvImage: ImageView = view.findViewById(R.id.iv_poster_image_tiket)

        fun bindItem(data: Film, listener: (Film) -> Unit, context : android.content.Context, position : Int) {

            tvTitle.text = data.title
            tvGenre.text = data.genre
            tvRate.text = data.rating

            Glide.with(context)
                .load(data.poster)
                .into(tvImage);

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }
}