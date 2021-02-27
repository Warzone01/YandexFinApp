package com.kirdevelopment.yandexfinapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StockList (
    @SerializedName("constituents")
    var constituents: List<String>
)

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




