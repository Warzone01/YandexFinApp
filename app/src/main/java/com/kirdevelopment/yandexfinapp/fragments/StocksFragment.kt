package com.kirdevelopment.yandexfinapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.listeners.StarClickListener
import com.kirdevelopment.yandexfinapp.presenters.StocksFragmentPresenter
import com.kirdevelopment.yandexfinapp.room.StocksEntity

class StocksFragment : Fragment(), StarClickListener {

    private lateinit var stocksRV: RecyclerView
    private lateinit var stocksProgress: CircularProgressIndicator

    lateinit var stocksPresenter: StocksFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stocks, container, false)

        stocksPresenter = StocksFragmentPresenter()

        stocksRV = view.findViewById(R.id.stocksRV)
        stocksProgress = view.findViewById(R.id.stocksProgress)

        stocksRV.layoutManager = LinearLayoutManager(view.context)

        stocksPresenter.getCurrentData(stocksRV, stocksProgress, view.context, this)

        return view
    }



    companion object {
        @JvmStatic
        fun newInstance() = StocksFragment()
    }

    override fun onStarClicked(stock: StocksEntity, position: Int, context: Context) {

        if (!stock.isFavourite){
            stocksPresenter.addStockToFavourite(stock, position, context, this)
        } else{
            println("Click2")
            stocksPresenter.deleteStockFromFavourite(stock, position, context, this)
        }

    }
}