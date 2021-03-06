package com.kirdevelopment.yandexfinapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.fragments.FavouriteFragment
import com.kirdevelopment.yandexfinapp.listeners.StarClickListener
import com.kirdevelopment.yandexfinapp.presenters.StocksFragmentPresenter
import com.kirdevelopment.yandexfinapp.room.StocksEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.stock_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

const val INSERT_STOCKS_TO_FAVOURITE = 1
const val DELETE_STOCKS_FROM_FAVOURITE = 2

class MainAdapter(private val stockItems: List<StocksEntity>,
                  private val clickListener: StarClickListener):
        RecyclerView.Adapter<MainAdapter.StocksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksViewHolder {
        return StocksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.stock_item, parent, false))
    }

    override fun onBindViewHolder(holder: StocksViewHolder, position: Int) {
        holder.bindStock(stockItems[position])

        val stockIsFavourite = holder.itemView.findViewById<ImageView>(R.id.addToFavouriteButton)

        val stocksFragmentPresenter = StocksFragmentPresenter()

        stockIsFavourite.setOnClickListener {
            clickListener.onStarClicked(stockItems[position], position, holder.itemView.context)
            notifyDataSetChanged()
        }

        //change background color for items
        stocksBackgroundColorChange(position, holder)
    }

    override fun getItemCount(): Int {
        return stockItems.size
    }

    class StocksViewHolder(view:View): RecyclerView.ViewHolder(view){
        //find views
        val stockLogo = view.findViewById<ImageView>(R.id.logoImage)
        val stockTicker = view.findViewById<TextView>(R.id.stockTickerText)
        val stockCompanyName = view.findViewById<TextView>(R.id.stockCompanyNameText)
        val stockPrice = view.findViewById<TextView>(R.id.stockPriceText)
        val stockPriceChange = view.findViewById<TextView>(R.id.stockPriceChangeText)
        val stockIsFavourite = view.findViewById<ImageView>(R.id.addToFavouriteButton)

        //all binds
        fun bindStock(stockItem: StocksEntity){
            val stocksFragmentPresenter = StocksFragmentPresenter()
            stockTicker.text = stockItem.ticker
            stockCompanyName.text = stockItem.name
            stockPrice.text = stockItem.currentPrice
            stockPriceChange.text = stockItem.previousPrice


            if (stockItem.isFavourite){
                stockIsFavourite.setImageResource(R.drawable.ic_star_active)
            } else{
                stockIsFavourite.setImageResource(R.drawable.ic_star)
            }

            try {
                Picasso.get()
                        .load(stockItem.logo)
                        .into(stockLogo)
            }catch (e:Exception){

            }

            if (stockItem.previousPrice.indexOf('-') >= 0){
                stockPriceChange.setTextColor("#B22424".toColorInt())
            }else{
                stockPriceChange.setTextColor("#24B25D".toColorInt())
            }
        }
    }

    fun stocksBackgroundColorChange(position: Int, holder: StocksViewHolder){
        val layout = holder.itemView.findViewById<ConstraintLayout>(R.id.stockItemLayout)
        if (position % 2 == 1){
            layout.setBackgroundResource(R.drawable.stock_background_white)
        }else{
            layout.setBackgroundResource(R.drawable.stock_background)
        }
    }
}