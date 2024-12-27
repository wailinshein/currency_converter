package com.example.currencyconverter.domain.repository

interface CurrencyConverterRepository {

    suspend fun getCurrencyList(): Map<String, String>

    suspend fun getExchangeRate(): Map<String, Double>

    suspend fun getSpecificRate(baseCurrency: String, targetCurrency: String): Double?
}




