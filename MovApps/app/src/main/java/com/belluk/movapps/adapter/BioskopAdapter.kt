package com.belluk.movapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.Bioskop
import com.belluk.movapps.model.Checkout

class BioskopAdapter(private var data: List<Bioskop>,
                     private val listener: (Bioskop) -> Unit)
    : RecyclerView.Adapter<BioskopAdapter.LeagueViewHolder>() {
    lateinit var ContextAdapter:Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView : View = layoutInflater.inflate(R.layout.item_bioskop,parent,false)
        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: BioskopAdapter.LeagueViewHolder, position: Int) {
        holder.bindItem(data[position],listener,ContextAdapter,position)
    }
    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private  var tvnamaBioskop:TextView = view.findViewById(R.id.nama_bioskop)

        fun bindItem(data:Bioskop,listener: (Bioskop) -> Unit,context: Context,position: Int){
            tvnamaBioskop.text = data.nama
            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

}