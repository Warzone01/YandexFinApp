package com.kirdevelopment.yandexfinapp.fragments

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.adapters.MainAdapter
import com.kirdevelopment.yandexfinapp.api.RetrofitInstance
import com.kirdevelopment.yandexfinapp.api.StockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat

private const val BASE_URL = "https://finnhub.io/api/v1/"
private const val TOKEN = "c0nk5uv48v6temfsrpdg"

class StocksFragment : Fragment() {

    private lateinit var stocksRV: RecyclerView
    private lateinit var stocksProgress: CircularProgressIndicator
    private var TAG = "StocksFragment"

    val service = RetrofitInstance.getStocks(BASE_URL).create(StockApi::class.java)

    val stocksItemsList: MutableList<String> = mutableListOf("IBM","VZ","AAPL","CRM","JNJ","AMGN","CAT","JPM","INTC","UNH","NKE","CSCO","WBA","AXP","PG","DOW","MMM","HON","WMT","BA","KO","MCD","MSFT","CVX","GS","V","MRK","DIS","TRV","HD")
    val stocksCurrentPrice: MutableList<String> = mutableListOf()
    val stocksPreviousPrice: MutableList<String> = mutableListOf()
    val stockNameList: MutableList<String> = mutableListOf()
    val stockLogoList: MutableList<String> = mutableListOf()

    val resLIst = stocksItemsList - 5

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

        stocksItemsList.sort()

        GlobalScope.launch(Dispatchers.IO) {
            try {

                for (i in stocksItemsList){
                //Get and show all stocks prices
                    val priceList = service.getStockPrice(i, TOKEN).awaitResponse()
                    try {
                        if (priceList.isSuccessful) {
                            val bodyPrice = priceList.body()
                            val priceCurrentItem = bodyPrice?.stockCurrentPrice
                            val pricePreviosItem = bodyPrice?.stockPreviousClosePrice
                            val df = DecimalFormat("#.##")
                            df.roundingMode = RoundingMode.FLOOR
                            val plusPrice = priceCurrentItem!! - pricePreviosItem!!
                            val minusPrice = pricePreviosItem - priceCurrentItem
                            val resOfPrices = if (priceCurrentItem > pricePreviosItem) "+$${df.format(plusPrice)}" else "-$${df.format(minusPrice)}"
                            stocksCurrentPrice.add(df.format(priceCurrentItem!!).toString())
                            stocksPreviousPrice.add(resOfPrices)

                            d(TAG, priceCurrentItem!!.toString())
                        }
                    } catch (e: Exception) {
                        d(TAG, "Error: $e")
                    }

                    //add profile name and image
                    val profileList = service.getStockProfile(i, TOKEN).awaitResponse()
                    try {
                        if (profileList.isSuccessful) {
                            val profileBody = profileList.body()
                            val profileName = profileBody?.name.toString()
                            val profileLogo = profileBody?.logo.toString()
                            stockNameList.add(profileName)
                            stockLogoList.add((profileLogo))

                            d(TAG, profileLogo)
                            d(TAG, profileName)
                        }
                    } catch (e: Exception) {
                        d(TAG, "Error: $e")
                    }
                }
                    withContext(Dispatchers.Main) {
                        stocksRV.adapter = MainAdapter(stocksItemsList, stocksCurrentPrice, stocksPreviousPrice, stockNameList, stockLogoList)
                        stocksRV.setHasFixedSize(true)
                        stocksRV.setItemViewCacheSize(30)
                        stocksRV.visibility = View.VISIBLE
                        stocksProgress.visibility = View.GONE
                    }
            }catch (e: Exception){
                d("Error", e.toString())
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = StocksFragment()
    }
}