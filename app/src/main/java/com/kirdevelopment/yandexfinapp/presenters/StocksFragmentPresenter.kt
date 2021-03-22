package com.kirdevelopment.yandexfinapp.presenters

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kirdevelopment.yandexfinapp.adapters.MainAdapter
import com.kirdevelopment.yandexfinapp.api.RetrofitInstance
import com.kirdevelopment.yandexfinapp.api.StockApi
import com.kirdevelopment.yandexfinapp.room.StocksDatabase
import com.kirdevelopment.yandexfinapp.room.StocksEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat


private const val BASE_URL = "https://finnhub.io/api/v1/"
private const val TOKEN = "c0nk5uv48v6temfsrpdg"

class StocksFragmentPresenter{

    private var TAG = "StocksFragment"
    private lateinit var database: StocksDatabase

    private var isFavourite = false

    //creating retrofit
    private val service = RetrofitInstance.getStocks(BASE_URL).create(StockApi::class.java)

    //all lists for stock items
    private val stocksItemsList: MutableList<String> = mutableListOf("IBM","VZ","AAPL","CRM","JNJ","AMGN",
            "CAT","JPM","INTC","UNH","NKE","CSCO","WBA","AXP","PG","DOW","MMM","HON","WMT","BA",
            "KO","MCD","MSFT","CVX","GS","V","MRK","DIS","TRV","HD")

    private var stocksList: ArrayList<StocksEntity> = ArrayList()

    var stocksCurrentPrice1: String = ""
    var stocksPreviousPrice1: String = ""
    var stocksName: String = ""
    var stocksLogo: String = ""

    fun getCurrentData(stocksRV: RecyclerView, cv: CircularProgressIndicator, context: Context){
        //sorting stocks for alphabet
        stocksItemsList.sort()
        //show load progress and hide stocks list
        showLoad(stocksRV, cv)

        database = StocksDatabase.getDatabase(context)

        //starting coroutine for add items from api
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val listStocks = StocksDatabase.getDatabase(context).stocksDao().getAllStocks()
                for (i in stocksItemsList) {
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

                            stocksCurrentPrice1 = df.format(priceCurrentItem).toString()
                            stocksPreviousPrice1 = resOfPrices


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

                            stocksName = profileName
                            stocksLogo = profileLogo

                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "Error: $e")
                    }

                    Log.d(TAG, i)
                    Log.d(TAG, stocksName)
                    Log.d(TAG, stocksLogo)
                    Log.d(TAG, stocksCurrentPrice1)
                    if(listStocks.size != stocksItemsList.size) {
                        saveToDb(stocksName, i, stocksLogo, stocksCurrentPrice1, stocksPreviousPrice1, isFavourite)
                    }
                }

                //add adapter and adding item in adapter
                withContext(Dispatchers.Main) {
                    //hide load progress and show stocks list
                    hideLoad(stocksRV, cv)
                    stocksList.addAll(listStocks)
                    stocksRV.adapter = MainAdapter(stocksList)
                }
            }catch (e: Exception){
                println(e.toString())
            }
        }
    }

    private fun showLoad(rv: RecyclerView, cv: CircularProgressIndicator){
        rv.visibility = View.GONE
        cv.visibility = View.VISIBLE
    }

    private fun hideLoad(rv: RecyclerView, cv: CircularProgressIndicator){
        rv.visibility = View.VISIBLE
        cv.visibility = View.GONE
    }

    private fun saveToDb(name: String,
                         ticker:String,
                         logo: String,
                         currentPrice: String,
                         previousPrice: String,
                         isFavourite: Boolean){

        val stock = StocksEntity()
        stock.name = name
        stock.ticker = ticker
        stock.currentPrice = currentPrice
        stock.previousPrice = previousPrice
        stock.logo = logo
        stock.isFavourite = isFavourite

        database.stocksDao().insertStocks(stock)

    }

    fun addToFavourite(context: Context){
        GlobalScope.launch(Dispatchers.IO) { val stock = StocksEntity()
            stock.isFavourite = true
            val updateDatabase = StocksDatabase.getDatabase(context).stocksDao().updateStocks(stock)
        }
    }

}