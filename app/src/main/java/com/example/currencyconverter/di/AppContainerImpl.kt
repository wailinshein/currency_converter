package com.example.currencyconverter.di

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.currencyconverter.data.local.database.CurrencyDatabase
import com.example.currencyconverter.data.network.CurrencyApiService
import com.example.currencyconverter.data.repository.CurrencyConverterRepositoryImpl
import com.example.currencyconverter.data.repository.OfflineRepositoryImpl
import com.example.currencyconverter.domain.repository.CurrencyConverterRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class AppContainerImpl(private val context: Context): AppContainer {

    override val currencyApiService: CurrencyApiService by lazy {

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CurrencyApiService.BASE_URL)
            .build()
            .create()
    }

    private val database: CurrencyDatabase by lazy {
        Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "currency_database"
        ).fallbackToDestructiveMigration()
        .build()
    }

    override val currencyConverterRepository: CurrencyConverterRepository by lazy {
        CurrencyConverterRepositoryImpl(currencyApiService)
    }

    override val offlineRepository: CurrencyConverterRepository by lazy {
        OfflineRepositoryImpl(database.dao)
    }

}