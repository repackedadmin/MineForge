package com.example.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PriceApiService {
    @GET("api/v3/ticker/price")
    suspend fun getTickerPrice(@Query("symbol") symbol: String = "ETHUSDT"): TickerResponse
}

data class TickerResponse(
    val symbol: String,
    val price: String
)

object PriceApiInstance {
    private const val BASE_URL = "https://api.binance.com/"

    val apiService: PriceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PriceApiService::class.java)
    }
}
