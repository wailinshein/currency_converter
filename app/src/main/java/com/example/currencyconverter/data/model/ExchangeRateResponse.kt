package com.example.currencyconverter.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponse(
    val success: Boolean,
    val source: String,
    val quotes: Map<String, Double>
)
