package com.example.currencyconverter.domain.repository

interface CacheRepository {

    suspend fun saveRate(baseCurrency: String, targetCurrency: String, rate: Double)

    suspend fun clearOldCaches()
}