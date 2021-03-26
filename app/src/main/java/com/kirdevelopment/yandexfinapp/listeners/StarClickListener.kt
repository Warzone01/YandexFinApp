package com.kirdevelopment.yandexfinapp.listeners

import android.content.Context
import com.kirdevelopment.yandexfinapp.room.StocksEntity

interface StarClickListener {

    fun onStarClicked(stock: StocksEntity, position: Int, context: Context)

}