package com.kirdevelopment.yandexfinapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "stocks")
data class StocksEntity(
        @PrimaryKey var ticker: String,
        @ColumnInfo(name = "name")var name: String,
        @ColumnInfo(name = "logo")var logo: String,
        @ColumnInfo(name = "current_price")var currentPrice: String,
        @ColumnInfo(name = "previous_price")var previousPrice: String,
        @ColumnInfo(name = "is_favourite")var isFavourite: Boolean ): Serializable{
constructor():this(
        "",
        "",
        "",
        "",
        "",
        false)
}




