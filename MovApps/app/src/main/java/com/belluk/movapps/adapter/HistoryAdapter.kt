package com.belluk.movapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.History
import com.belluk.movapps.utils.CustomClickListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_item_tiket.view.*

class HistoryAdapter :RecyclerView.Adapter<HistoryAdapter.MoviewViewHolder>() {
    var listTiket = ArrayList<History>()
    set(listTiket){
        if (listTiket.size > 0){
            this.listTiket.clear()
        }
        this.listTiket.addAll(listTiket)
        notifyDataSetChanged()
    }
    fun addItem(tiket: History){
        this.listTiket.add(tiket)
        notifyItemInserted(this.listTiket.size -1)
    }
    fun updateItem(position: Int,tiket: History){
        this.listTiket[position] = tiket
        notifyItemChanged(position,tiket)
    }
    fun deleteItem(position: Int){
        this.listTiket.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position,this.listTiket.size)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.MoviewViewHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_tiket,parent,false)
        return MoviewViewHolder(view)
    }

    override fun getItemCount(): Int = this.listTiket.size

    override fun onBindViewHolder(holder: MoviewViewHolder, position: Int) {
        holder.bind(listTiket[position])
    }
    inner class MoviewViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(tiket: History){
            with(itemView){
                tv_title_tiket_detail.text = tiket.nama
                tv_genre_tiket.text = tiket.genre
                tv_kode.text = "Ticket Code : "+tiket.kodeTiket
                tv_rate_tiket_detail.text = tiket.rating
                tv_date_buy.text = tiket.date
                Glide.with(context).load(tiket.poster).into(iv_poster_image_tiket)
                card.setOnClickListener(CustomClickListener(adapterPosition,object :CustomClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                    }

                }))
            }
        }
    }
}