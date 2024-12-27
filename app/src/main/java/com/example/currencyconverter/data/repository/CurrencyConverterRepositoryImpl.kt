package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.network.CurrencyApiService
import com.example.currencyconverter.domain.repository.CurrencyConverterRepository

class CurrencyConverterRepositoryImpl(
    private val apiService: CurrencyApiService,
): CurrencyConverterRepository {
    private val apiKey = "9b321ef90620c7cfe32602111c15f0e4"
    override suspend fun getCurrencyList(): Map<String, String> {
        return try {
            val res = apiService.getCurrencyList(apiKey)
            if(res.body()!!.success) {
                res.body()?.currencies?: emptyMap()
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    override suspend fun getExchangeRate(): Map<String, Double> {
        return try {
            val res = apiService.getExchangeRates(apiKey)
            if(res.body()!!.success) {
                res.body()?.quotes?: emptyMap()
            } else {
                emptyMap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }

    override suspend fun getSpecificRate(
        baseCurrency: String,
        targetCurrency: String
    ): Double? {
        val currencies = listOf(baseCurrency, targetCurrency).joinToString(",")
        return try {
            val res = apiService.getSpecificRates(apiKey, currencies)
            if(res.body()!!.success) {
                val rates = res.body()!!.quotes
                val baseRate = rates["USD$baseCurrency"]
                val targetRate = rates["USD$targetCurrency"]
                when {
                    baseCurrency == "USD" -> {
                        targetRate
                    }
                    targetCurrency == "USD" -> {
                        baseRate?.let { 1 / it }
                    }
                    baseRate != null && targetRate != null -> {
                        targetRate / baseRate
                    }
                    else -> {
                        throw Exception("Missing rate for $baseCurrency and $targetCurrency")
                    }
                }
            } else {
                throw Exception("Error fetching data")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}