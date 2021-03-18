package com.kirdevelopment.yandexfinapp.presenters

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.yandexfinapp.adapters.FavouriteAdapter
import com.kirdevelopment.yandexfinapp.room.StocksDatabase
import com.kirdevelopment.yandexfinapp.room.StocksEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragmentPresenter {

    private val TAG: String = "DEBUG MSG"
    private lateinit var stocksDatabase: StocksDatabase

    private var favouriteList: ArrayList<StocksEntity> = ArrayList()

    fun getCurrentData(context:Context, rv: RecyclerView, textEmpty: TextView){
        stocksDatabase = StocksDatabase.getDatabase(context)

        GlobalScope.launch(Dispatchers.IO) {
            val favouriteStocksList = StocksDatabase.getDatabase(context).stocksDao().getAllStocks()

            withContext(Dispatchers.Main) {
                favouriteList.addAll(favouriteStocksList)
                rv.adapter = FavouriteAdapter(favouriteList)
            }
        }
    }

    fun listEmpty(rv: RecyclerView, textEmpty: TextView){
        rv.visibility = View.GONE
        textEmpty.visibility = View.VISIBLE
    }

}