package com.kirdevelopment.yandexfinapp.api

import com.kirdevelopment.yandexfinapp.model.StockItem
import com.kirdevelopment.yandexfinapp.model.StockList
import retrofit2.Call
import retrofit2.http.GET

interface StockApi {

    @GET("index/constituents?symbol=^DJI&token=c0nk5uv48v6temfsrpdg")
    fun getStockList(): Call<StockList>

}