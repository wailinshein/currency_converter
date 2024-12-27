package com.example.currencyconverter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.data.local.model.ExchangeRateEntity

@Dao
interface CurrencyRateDao {

    @Query("SELECT rate FROM exchange_rate WHERE currencyPair = :currencyPair AND timestamp > :validTime")
    suspend fun getRate(currencyPair: String, validTime: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(exchangeRate: ExchangeRateEntity)

    @Query("DELETE FROM exchange_rate WHERE timestamp < :validTime")
    suspend fun clearOldCaches(validTime: Long)
}