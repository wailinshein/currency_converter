package com.example.currencyconverter.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyListResponse (
    val success: Boolean,
    val currencies: Map<String, String>
)