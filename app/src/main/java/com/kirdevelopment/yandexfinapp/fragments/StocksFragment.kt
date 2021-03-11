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

    //creating retrofit
    val service = RetrofitInstance.getStocks(BASE_URL).create(StockApi::class.java)

    //all lists for stock items
    val stocksItemsList: MutableList<String> = mutableListOf("IBM","VZ","AAPL","CRM","JNJ","AMGN",
            "CAT","JPM","INTC","UNH","NKE","CSCO","WBA","AXP","PG","DOW","MMM","HON","WMT","BA",
            "KO","MCD","MSFT","CVX","GS","V","MRK","DIS","TRV","HD")
    val stocksCurrentPrice: MutableList<String> = mutableListOf()
    val stocksPreviousPrice: MutableList<String> = mutableListOf()
    val stockNameList: MutableList<String> = mutableListOf()
    val stockLogoList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stocks, container, false)

        stocksRV = view.findViewById(R.id.stocksRV)
        stocksProgress = view.findViewById(R.id.stocksProgress)

        stocksRV.layoutManager = LinearLayoutManager(view.context)
        stocksRV.setHasFixedSize(true)

        //show progress circle and hide adapter
        stocksProgress.visibility = View.VISIBLE
        stocksRV.visibility = View.GONE

        getCurrentData()
        return view
    }

    private fun getCurrentData(){
        //sorting stocks for alphabet
        stocksItemsList.sort()

        //starting coroutine for add items from api
        GlobalScope.launch(Dispatchers.IO) {
            try {

                for (i in stocksItemsList){
                //Get and show all stocks prices
                    val priceList = service.getStockPrice(i, TOKEN).awaitResponse()
                    try {
                        if (priceList.isSuccessful) {
                            val bodyPrice = priceList.body()
                            //get current price
                            val priceCurrentItem = bodyPrice?.stockCurrentPrice
                            //get previous price
                            val pricePreviousItem = bodyPrice?.stockPreviousClosePrice
                            //pattern for price
                            val df = DecimalFormat("#.##")
                            df.roundingMode = RoundingMode.FLOOR
                            //add operator for previous price
                            val plusPrice = priceCurrentItem!! - pricePreviousItem!!
                            val minusPrice = pricePreviousItem - priceCurrentItem
                            val resOfPrices = if (priceCurrentItem > pricePreviousItem) "+$${df.format(plusPrice)}" else "-$${df.format(minusPrice)}"
                            //add current price in list
                            stocksCurrentPrice.add(df.format(priceCurrentItem!!).toString())
                            //add previous price in list
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
                            //get profile name
                            val profileName = profileBody?.name.toString()
                            //get profile logo image
                            val profileLogo = profileBody?.logo.toString()
                            //add profileName in list
                            stockNameList.add(profileName)
                            //add profile logo image in list
                            stockLogoList.add((profileLogo))

                            d(TAG, profileLogo)
                            d(TAG, profileName)
                        }
                    } catch (e: Exception) {
                        d(TAG, "Error: $e")
                    }
                }

                    //add adapter and adding item in adapter
                    withContext(Dispatchers.Main) {
                        stocksRV.adapter = MainAdapter(stocksItemsList, stocksCurrentPrice, stocksPreviousPrice, stockNameList, stockLogoList)
                        //add image for cache
                        stocksRV.setHasFixedSize(true)
                        stocksRV.setItemViewCacheSize(30)
                        //hide progress circle and show adapter
                        stocksRV.visibility = View.VISIBLE
                        stocksProgress.visibility = View.GONE
                    }
            }catch (e: Exception){
                getCurrentData()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = StocksFragment()
    }
}