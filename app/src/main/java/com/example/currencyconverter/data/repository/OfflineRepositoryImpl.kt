package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.local.dao.CurrencyRateDao
import com.example.currencyconverter.data.local.model.ExchangeRateEntity
import com.example.currencyconverter.domain.repository.CacheRepository
import com.example.currencyconverter.domain.repository.CurrencyConverterRepository

class OfflineRepositoryImpl(
    private val currencyRateDao: CurrencyRateDao
): CurrencyConverterRepository, CacheRepository {

    companion object {
        const val CACHE_DURATION = 30 * 60 * 1000
    }

    override suspend fun getCurrencyList(): Map<String, String> {
        return emptyMap()
    }

    override suspend fun getExchangeRate(): Map<String, Double> {
        return emptyMap()
    }

    override suspend fun getSpecificRate(baseCurrency: String, targetCurrency: String): Double? {
        val currencyPair = "$baseCurrency$targetCurrency"
        val currentTime = System.currentTimeMillis()

        return currencyRateDao.getRate(currencyPair, currentTime - CACHE_DURATION)
    }

    override suspend fun saveRate(baseCurrency: String, targetCurrency: String, rate: Double) {
        val currencyPair = "$baseCurrency$targetCurrency"
        val currentTime = System.currentTimeMillis()
        currencyRateDao.insertRate(
            ExchangeRateEntity(currencyPair, rate, currentTime)
        )
    }

    override suspend fun clearOldCaches() {
        val currentTime = System.currentTimeMillis()
        currencyRateDao.clearOldCaches(currentTime - CACHE_DURATION)
    }

}