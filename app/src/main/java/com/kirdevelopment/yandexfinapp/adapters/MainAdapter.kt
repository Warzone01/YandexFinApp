package com.kirdevelopment.yandexfinapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.yandexfinapp.R
import com.squareup.picasso.Picasso
import java.lang.Exception

class MainAdapter(private val stockItems: MutableList<String>,
                  private val stockCurrentPrice: MutableList<String>,
                  private val stocksPreviousPrice: MutableList<String>,
                  private val stocksName: MutableList<String>,
                  private val stocksLogo: MutableList<String>):
        RecyclerView.Adapter<MainAdapter.StocksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksViewHolder {
        return StocksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.stock_item, parent, false))
    }

    override fun onBindViewHolder(holder: StocksViewHolder, position: Int) {
        holder.bindTicker(stockItems[position])
        holder.bindCurrentPrice(stockCurrentPrice[position])
        holder.bindPreviousPrice(stocksPreviousPrice[position])
        holder.bindStocksName(stocksName[position])
        holder.bindStockLogo(stocksLogo[position])

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
        private val stockLogo = view.findViewById<ImageView>(R.id.logoImage)
        private val stockTicker = view.findViewById<TextView>(R.id.stockTickerText)
        private val stockCompanyName = view.findViewById<TextView>(R.id.stockCompanyNameText)
        private val stockPrice = view.findViewById<TextView>(R.id.stockPriceText)
        private val stockPriceChange = view.findViewById<TextView>(R.id.stockPriceChangeText)

        fun bindTicker(stockItem: String){
            stockTicker.text = stockItem
        }

        fun bindCurrentPrice(c: String){
            stockPrice.text = "$$c"
        }

        fun bindPreviousPrice(pc: String){
            stockPriceChange.text = pc
            if (pc.indexOf('-') >= 0){
                stockPriceChange.setTextColor("#B22424".toColorInt())
            } else {
                stockPriceChange.setTextColor("#24B25D".toColorInt())
            }
        }
        fun bindStocksName(name: String){
            stockCompanyName.text = name
        }

        fun bindStockLogo(logo: String){
            try {
                Picasso.get()
                        .load(logo)
                        .into(stockLogo)
            }catch (e:Exception){

            }
        }
    }
}