package com.example.currencyconverter

import android.app.Application
import com.example.currencyconverter.di.AppContainer
import com.example.currencyconverter.di.AppContainerImpl

class CurrencyConverterApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(applicationContext)
    }
}