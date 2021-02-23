package com.kirdevelopment.yandexfinapp.fragments

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.adapters.MainAdapter
import com.kirdevelopment.yandexfinapp.api.RetrofitInstance
import com.kirdevelopment.yandexfinapp.api.StockApi
import com.kirdevelopment.yandexfinapp.model.StockItem
import com.kirdevelopment.yandexfinapp.model.StockList
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

private const val BASE_URL = "https://finnhub.io/api/v1/"

class StocksFragment : Fragment() {

    private lateinit var stocksRV: RecyclerView
    private lateinit var stocksProgress: CircularProgressIndicator
    private var TAG = "StocksFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stocks, container, false)

        stocksRV = view.findViewById(R.id.stocksRV)
        stocksProgress = view.findViewById(R.id.stocksProgress)

        stocksRV.layoutManager = LinearLayoutManager(view.context)
        stocksRV.setHasFixedSize(true)

        stocksProgress.visibility = View.VISIBLE
        stocksRV.visibility = View.GONE

        getCurrentData()

        return view
    }

    private fun getCurrentData(){

        val service = RetrofitInstance.getStocks(BASE_URL).create(StockApi::class.java)
        val stocks = service.getStockList()
        stocks.enqueue(object : Callback<StockList> {
            override fun onResponse(call: Call<StockList>, response: Response<StockList>) {

                val body = response.body()
                val stocksList = body?.constituents
                var size = stocksList?.size
                val stocksItemsList: MutableList<String> = mutableListOf()


                    for (i in stocksList!!) {
                        stocksItemsList.add(i)
                        stocksRV.adapter = MainAdapter(stocksItemsList)
                        d(TAG, "ListOf $i")
                    }


                stocksRV.visibility = View.VISIBLE
                stocksProgress.visibility = View.GONE

            }

            override fun onFailure(call: Call<StockList>, t: Throwable) {
                d(TAG, "ERRO000000R")
                getCurrentData()
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance() = StocksFragment()
    }
}