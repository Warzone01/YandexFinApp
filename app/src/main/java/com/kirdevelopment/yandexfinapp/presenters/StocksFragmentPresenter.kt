package com.kirdevelopment.yandexfinapp.presenters

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kirdevelopment.yandexfinapp.adapters.MainAdapter
import com.kirdevelopment.yandexfinapp.api.RetrofitInstance
import com.kirdevelopment.yandexfinapp.api.StockApi
import com.kirdevelopment.yandexfinapp.views.StocksView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.awaitResponse
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat


private const val BASE_URL = "https://finnhub.io/api/v1/"
private const val TOKEN = "c0nk5uv48v6temfsrpdg"

class StocksFragmentPresenter{

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

    fun getCurrentData(stocksRV: RecyclerView, cv: CircularProgressIndicator){
        //sorting stocks for alphabet
        stocksItemsList.sort()
        //show load progress and hide stocks list
        showLoad(stocksRV, cv)
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

                            Log.d(TAG, priceCurrentItem!!.toString())
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "Error: $e")
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

                            Log.d(TAG, profileLogo)
                            Log.d(TAG, profileName)
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "Error: $e")
                    }
                }

                //add adapter and adding item in adapter
                withContext(Dispatchers.Main) {
                    //hide load progress and show stocks list
                    hideLoad(stocksRV, cv)
                    stocksRV.adapter = MainAdapter(stocksItemsList, stocksCurrentPrice, stocksPreviousPrice, stockNameList, stockLogoList)
                    //add image cache
                    stocksRV.setHasFixedSize(true)
                    stocksRV.setItemViewCacheSize(30)

                }
            }catch (e: Exception){

            }
        }
    }

    fun showLoad(rv: RecyclerView, cv: CircularProgressIndicator){
        rv.visibility = View.GONE
        cv.visibility = View.VISIBLE
    }

    fun hideLoad(rv: RecyclerView, cv: CircularProgressIndicator){
        rv.visibility = View.VISIBLE
        cv.visibility = View.GONE
    }


}