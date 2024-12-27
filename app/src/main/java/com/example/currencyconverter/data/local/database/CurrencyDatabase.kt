package com.example.currencyconverter.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconverter.data.local.dao.CurrencyRateDao
import com.example.currencyconverter.data.local.model.ExchangeRateEntity

@Database(entities = [ExchangeRateEntity::class], version = 2)
abstract class CurrencyDatabase: RoomDatabase() {

    abstract val dao: CurrencyRateDao

}