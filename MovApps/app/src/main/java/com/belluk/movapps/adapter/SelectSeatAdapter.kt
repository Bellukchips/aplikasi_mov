package com.belluk.movapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.Bioskop
import com.belluk.movapps.model.Checkout
import com.belluk.movapps.model.Seat
import com.google.android.material.snackbar.Snackbar

class SelectSeatAdapter(private var data: List<Seat>,
                        private val listener: (Seat) -> Unit)
    : RecyclerView.Adapter<SelectSeatAdapter.LeagueViewHolder>() {
    private lateinit var ContextAdapter:Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView : View = layoutInflater.inflate(R.layout.row_item_seat,parent,false)
        return LeagueViewHolder(inflatedView)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: SelectSeatAdapter.LeagueViewHolder, position: Int) {
        holder.bindItem(data[position],listener,ContextAdapter,position)
    }
    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private  var tvnama:TextView = view.findViewById(R.id.tv_no_seat)
            private  var img:ImageView = view.findViewById(R.id.img_seat)
            private var check:Boolean = false
        fun bindItem(data:Seat,listener: (Seat) -> Unit,context: Context,position: Int){
            tvnama.text = data.nama
            if (data.status == "1"){
                img.setImageResource(R.drawable.shape_booked_)
            }
            itemView.setOnClickListener {
                listener(data)
               if (check){
                   check = false
                   data.checked = false
                   img.setImageResource(R.drawable.shape_line_grey)
               }else{
                   check = true
                   data.checked = true
                   img.setImageResource(R.drawable.shape_selected)
               }
            }
            if (data.status == "1"){
                itemView.setOnClickListener {
                 val snackbar =   Snackbar.make(itemView,"Kursi Sudah Di Booking",Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        }
    }

}