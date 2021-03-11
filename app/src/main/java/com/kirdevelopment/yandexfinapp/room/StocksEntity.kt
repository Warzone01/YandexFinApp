package com.kirdevelopment.yandexfinapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StocksEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val ticker: String,
        val name: String,
        val logo: String,
        val currentPrice: String,
        val previousPrice: String,
        val isFavourite: Boolean )




