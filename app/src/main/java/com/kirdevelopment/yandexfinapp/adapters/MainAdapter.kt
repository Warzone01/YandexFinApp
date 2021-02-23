package com.kirdevelopment.yandexfinapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.model.StockItem
import com.kirdevelopment.yandexfinapp.model.StockList

class MainAdapter(private val stockItems: MutableList<String>): RecyclerView.Adapter<MainAdapter.StocksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksViewHolder {
        return StocksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.stock_item, parent, false))
    }

    override fun onBindViewHolder(holder: StocksViewHolder, position: Int) {
        holder.bind(stockItems[position])
        val layout = holder.itemView.findViewById<ConstraintLayout>(R.id.stockItemLayout)
        if (position % 2 == 1){
            layout.setBackgroundResource(R.drawable.stock_background_white)
        }else{
            layout.setBackgroundResource(R.drawable.stock_background)
        }
    }

    override fun getItemCount(): Int {
        return stockItems.size
    }

    class StocksViewHolder(view:View): RecyclerView.ViewHolder(view){
        private val imageLogo = view.findViewById<ImageView>(R.id.logoImage)
        private val stockTicker = view.findViewById<TextView>(R.id.stockTickerText)
        private val stockCompanyName = view.findViewById<TextView>(R.id.stockCompanyNameText)
        private val stockPrice = view.findViewById<TextView>(R.id.stockPriceText)
        private val stockPriceChange = view.findViewById<TextView>(R.id.stockPriceChangeText)

        fun bind(stockItem: String){
            stockTicker.text = stockItem
        }
    }

}