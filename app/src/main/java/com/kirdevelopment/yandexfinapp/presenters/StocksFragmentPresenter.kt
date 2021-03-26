package com.kirdevelopment.yandexfinapp.presenters

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kirdevelopment.yandexfinapp.adapters.MainAdapter
import com.kirdevelopment.yandexfinapp.api.RetrofitInstance
import com.kirdevelopment.yandexfinapp.api.StockApi
import com.kirdevelopment.yandexfinapp.listeners.StarClickListener
import com.kirdevelopment.yandexfinapp.room.StocksDatabase
import com.kirdevelopment.yandexfinapp.room.StocksEntity
import com.kirdevelopment.yandexfinapp.views.StocksView
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
    private lateinit var favouriteDatabase: StocksDatabase
    private lateinit var favouriteAdapter: MainAdapter
    private lateinit var stocksAdapter: MainAdapter

    private val requestCodeAddToFavourite = 1
    private val requestCodeDeleteFromFavourite = 2

    private var requestCode = 0

    private var stockClickedPosition = -1

    //creating retrofit
    private val service = RetrofitInstance.getStocks(BASE_URL).create(StockApi::class.java)

    //all lists for stock items
    private val stocksItemsList: MutableList<String> = mutableListOf("IBM","VZ","AAPL","CRM","JNJ","AMGN",
            "CAT","JPM","INTC","UNH","NKE","CSCO","WBA","AXP","PG","DOW","MMM","HON","WMT","BA",
            "KO","MCD","MSFT","CVX","GS","V","MRK","DIS","TRV","HD")

    private var stocksList: ArrayList<StocksEntity> = ArrayList()
    private var favouriteList: ArrayList<StocksEntity> = ArrayList()

    var stocksCurrentPrice1: String = ""
    var stocksPreviousPrice1: String = ""
    var stocksName: String = ""
    var stocksLogo: String = ""

    fun getCurrentData(stocksRV: RecyclerView, cv: CircularProgressIndicator, context: Context, clickListener: StarClickListener){
//        //sorting stocks for alphabet
//        stocksItemsList.sort()
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

                            stocksCurrentPrice1 = "$${df.format(priceCurrentItem)}"
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
                        saveToDb(stocksName, i, stocksLogo, stocksCurrentPrice1, stocksPreviousPrice1)
                    }
                }

                //add adapter and adding item in adapter
                withContext(Dispatchers.Main) {
                    //hide load progress and show stocks list
                    hideLoad(stocksRV, cv)
                    stocksList.addAll(listStocks)
                    stocksAdapter = MainAdapter(stocksList, clickListener)
                    stocksRV.adapter = stocksAdapter
                    stocksAdapter.notifyItemRangeInserted(0, stocksList.size)
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
                         previousPrice: String){

        val stock = StocksEntity()
        stock.name = name
        stock.ticker = ticker
        stock.currentPrice = currentPrice
        stock.previousPrice = previousPrice
        stock.logo = logo

        database.stocksDao().insertStocks(stock)

    }



    fun getAllFavourites(favouriteRV: RecyclerView, context: Context, textEmpty: TextView, clickListener: StarClickListener){
        GlobalScope.launch(Dispatchers.IO) {
            val listFavourites = StocksDatabase.getDatabase(context).stocksDao().getAllFavourites()
            withContext(Dispatchers.Main){
                favouriteList.addAll(listFavourites)
                favouriteAdapter = MainAdapter(favouriteList, clickListener)
                favouriteRV.adapter = favouriteAdapter
                if (favouriteList.isEmpty()){
                    textEmpty.visibility = View.VISIBLE
                    favouriteRV.visibility = View.GONE
                } else {
                    favouriteRV.visibility = View.VISIBLE
                    textEmpty.visibility = View.GONE
                }
            }
        }
    }


    fun addStockToFavourite(stock: StocksEntity, position:Int, context: Context, clickListener: StarClickListener){
        GlobalScope.launch(Dispatchers.IO) {
            favouriteDatabase = StocksDatabase.getDatabase(context)
            stock.isFavourite = true
            favouriteDatabase.stocksDao().updateStocks(stock)
            val listFavourites = StocksDatabase.getDatabase(context).stocksDao().getAllFavourites()
            withContext(Dispatchers.Main){
                println("Click1")
                for (i in favouriteList){
                    println("Before $i")
                }
                favouriteList.clear()
                favouriteList.addAll(listFavourites)
                for (i in favouriteList){
                    println("AFter $i")
                }
                favouriteAdapter = MainAdapter(favouriteList, clickListener)
                favouriteAdapter.notifyItemRangeRemoved(0, favouriteList.size)
                favouriteAdapter.notifyItemRangeInserted(0, favouriteList.size)
            }
        }

    }
    fun deleteStockFromFavourite(stock: StocksEntity, position:Int, context: Context, clickListener: StarClickListener){
        GlobalScope.launch(Dispatchers.IO) {
            favouriteDatabase = StocksDatabase.getDatabase(context)
            stock.isFavourite = false
            favouriteDatabase.stocksDao().updateStocks(stock)
            withContext(Dispatchers.Main){
                println("Click2")
                favouriteList.removeAt(position)
                favouriteAdapter = MainAdapter(favouriteList, clickListener)
                favouriteAdapter.notifyItemRemoved(position)
                favouriteAdapter.notifyItemChanged(position)
            }
        }
    }
}

