package com.example.currencyconverter.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity(
    @PrimaryKey val currencyPair: String,
    val rate: Double,
    val timestamp: Long
)