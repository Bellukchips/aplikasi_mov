package com.belluk.movapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.Plays
import com.belluk.movapps.model.Seat
import com.belluk.movapps.model.Tiket
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SeatAdapter(private var data: List<Seat>,
                  private val listener: (Seat) -> Unit)
    : RecyclerView.Adapter<SeatAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter : Context
    private var detail:Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_detail_tiket, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var tv_kursi = view.findViewById<TextView>(R.id.tv_kursi)

        fun bindItem(data: Seat, listener: (Seat) -> Unit, context : Context, position : Int) {
            tv_kursi.text = "Seat No "+data.nama
            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}

