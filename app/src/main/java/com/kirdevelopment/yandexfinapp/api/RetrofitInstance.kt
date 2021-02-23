package com.kirdevelopment.yandexfinapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private var retrofit: Retrofit? = null

    fun getStocks(baseUrl: String): Retrofit {
        if (retrofit == null){
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }

}