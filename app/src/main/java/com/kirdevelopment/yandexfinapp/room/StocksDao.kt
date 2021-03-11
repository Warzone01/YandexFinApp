package com.kirdevelopment.yandexfinapp.room

import androidx.room.*

@Dao
interface StocksDao {
    @Query("SELECT * FROM stocks ORDER BY id DESC")
    fun getAllStocks():List<StocksEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStocks(stocks: StocksEntity)

    @Update
    fun updateStocks(stocks: StocksEntity)
}