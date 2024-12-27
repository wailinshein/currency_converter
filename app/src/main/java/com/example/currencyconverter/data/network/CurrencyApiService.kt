package com.example.currencyconverter.data.network

import com.example.currencyconverter.data.model.CurrencyListResponse
import com.example.currencyconverter.data.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {

    companion object {
        const val BASE_URL = "https://api.currencylayer.com/"
    }

    @GET("/list")
    suspend fun getCurrencyList(
        @Query("access_key") apiKey: String
    ): Response<CurrencyListResponse>

    @GET("/live")
    suspend fun getExchangeRates(
        @Query("access_key") apiKey: String
    ): Response<ExchangeRateResponse>

    @GET("/live")
    suspend fun getSpecificRates(
        @Query("access_key") apiKey: String,
        @Query("currencies") currencies: String
    ): Response<ExchangeRateResponse>

}