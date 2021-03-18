
package com.kirdevelopment.yandexfinapp.api

//import com.kirdevelopment.yandexfinapp.model.StockList
import com.kirdevelopment.yandexfinapp.model.StockPrice
import com.kirdevelopment.yandexfinapp.model.StockProfile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

//    @GET("index/constituents?symbol=^DJI")
//    fun getStockList(@Query("token")token: String): Call<StockList>

    @GET("quote")
    fun getStockPrice(@Query("symbol") ticker:String, @Query("token")token:String): Call<StockPrice>

    @GET("stock/profile2")
    fun getStockProfile(@Query("symbol") ticker: String, @Query("token")token:String): Call<StockProfile>
}