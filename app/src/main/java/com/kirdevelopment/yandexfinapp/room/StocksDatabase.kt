package com.kirdevelopment.yandexfinapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StocksEntity::class], version = 1, exportSchema = false)
abstract class StocksDatabase: RoomDatabase() {
    abstract fun stocksDao(): StocksDao
    companion object {
        private var stocksDatabase: StocksDatabase? = null
        fun getDatabase(context: Context):StocksDatabase {
            if (stocksDatabase == null){
                stocksDatabase = Room.databaseBuilder(
                        context,
                        StocksDatabase::class.java,
                        "stocksDB").build()
            }
            return stocksDatabase as StocksDatabase
        }

    }


}