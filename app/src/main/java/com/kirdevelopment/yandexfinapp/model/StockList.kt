package com.kirdevelopment.yandexfinapp.model

import com.google.gson.annotations.SerializedName

//data class StockList (
//    @SerializedName("constituents")
//    var constituents: MutableList<String>
//)

data class StockPrice(
    @SerializedName("c")
    var stockCurrentPrice: Double,
    @SerializedName("pc")
    var stockPreviousClosePrice: Double
)

data class StockProfile (
    @SerializedName("name")
    var name: String,
    @SerializedName("logo")
    var logo: String

)




