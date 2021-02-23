package com.kirdevelopment.yandexfinapp.common

import com.kirdevelopment.yandexfinapp.api.RetrofitInstance
import com.kirdevelopment.yandexfinapp.api.StockApi
import retrofit2.Retrofit

object Common {
    private const val BASE_URL = "https://finnhub.io/api/v1/"
    val stockApi: RetrofitInstance?
        get() = RetrofitInstance.getStocks(BASE_URL).create(RetrofitInstance::class.java)
}