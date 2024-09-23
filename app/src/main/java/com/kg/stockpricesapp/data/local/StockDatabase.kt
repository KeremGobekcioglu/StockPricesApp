package com.kg.stockpricesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities =[CompanyListingEntity::class],
    version = 1
)
abstract class StockDatabase : RoomDatabase(){
    abstract val stockDao : StockDao
    companion object
    {
        const val name : String = "STOCK_DB"
    }
}