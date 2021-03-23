package com.kirdevelopment.yandexfinapp.room

import androidx.room.*

@Dao
interface StocksDao {
    @Query("SELECT * FROM stocks ORDER BY ticker ASC")
    fun getAllStocks():List<StocksEntity>

    @Query("SELECT * FROM stocks WHERE is_favourite")
    fun getAllFavourites():List<StocksEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStocks(stocks: StocksEntity)

    @Update
    fun updateStocks(stocks: StocksEntity)

    @Delete
    fun deleteFavourite(stocks: StocksEntity)
}