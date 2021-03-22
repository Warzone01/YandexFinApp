package com.kirdevelopment.yandexfinapp.room

import androidx.room.*

@Dao
interface StocksDao {
    @Query("SELECT * FROM stocks ORDER BY id ASC")
    fun getAllStocks():List<StocksEntity>

    @Query("SELECT * FROM stocks WHERE is_favourite")
    fun getAllFavourites():List<StocksEntity>

    @Insert
    fun insertStocks(stocks: StocksEntity)

    @Update
    fun updateStocks(stocks: StocksEntity)

    @Delete
    fun deleteFavourite(stocks: StocksEntity)
}