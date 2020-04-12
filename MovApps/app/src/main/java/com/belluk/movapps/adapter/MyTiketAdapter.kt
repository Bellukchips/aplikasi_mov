package com.belluk.movapps.adapter

import android.content.Context
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.Tiket
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource

class MyTiketAdapter(private var data: List<Tiket>,
                     private val listener: (Tiket) -> Unit)
    : RecyclerView.Adapter<MyTiketAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_tiket, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTitle: TextView = view.findViewById(R.id.tv_title_tiket_detail)
        private val tvTGenre: TextView = view.findViewById(R.id.tv_genre_tiket)
        private val tvrate: TextView = view.findViewById(R.id.tv_rate_tiket_detail)
        private val tvKode: TextView = view.findViewById(R.id.tv_kode)
        private val tv_date: TextView = view.findViewById(R.id.tv_date_buy)
        private val img: ImageView = view.findViewById(R.id.iv_poster_image_tiket)

        fun bindItem(data: Tiket, listener: (Tiket) -> Unit, context : Context, position : Int) {
            tvTitle.text = data.nama
            tvTGenre.text = data.genre
            tvrate.text = data.rating
            tv_date.text = data.date
            tvKode.text = itemView.resources.getString(R.string.ticket_code) +" : "+ data.kodeTiket
            Glide.with(context).load(data.poster).into(img)
            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}

