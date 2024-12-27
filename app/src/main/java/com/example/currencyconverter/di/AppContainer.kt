package com.example.currencyconverter.di

import com.example.currencyconverter.data.network.CurrencyApiService
import com.example.currencyconverter.domain.repository.CurrencyConverterRepository

interface AppContainer {
    val currencyApiService: CurrencyApiService
    val currencyConverterRepository: CurrencyConverterRepository
    val offlineRepository: CurrencyConverterRepository
}