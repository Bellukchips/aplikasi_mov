package com.belluk.movapps.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.model.Wallet
import java.text.NumberFormat
import java.util.*

class WalletAdapter(private var data:List<Wallet>,private var listener:(Wallet)->Unit):RecyclerView.Adapter<WalletAdapter.LegueViewHolder>() {
    lateinit var ContextAdapter : Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WalletAdapter.LegueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val infaltedView = layoutInflater.inflate(R.layout.row_item_transaksi,parent,false)
        return LegueViewHolder(infaltedView)
    }

    override fun getItemCount(): Int= data.size

    override fun onBindViewHolder(holder: WalletAdapter.LegueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }
    class LegueViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val tv_movie = view.findViewById<TextView>(R.id.tv_movie)
        private val tv_money = view.findViewById<TextView>(R.id.tv_money)
        private val tv_date = view.findViewById<TextView>(R.id.tv_date)
        fun bindItem(data:Wallet, listener: (Wallet) -> Unit, context: Context, position: Int){
            tv_movie.text = data.title
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            tv_date.text = data.date
            if (data.status.equals("0")){
                tv_money.text = "- "+formatRupiah.format(data.money!!.toDouble())
            }else{
                tv_money.text = "+ "+formatRupiah.format(data.money!!.toDouble())
                tv_money.setTextColor(Color.GREEN)
            }
        }

    }
}