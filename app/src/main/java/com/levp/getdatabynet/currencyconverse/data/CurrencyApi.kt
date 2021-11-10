package com.levp.getdatabynet.currencyconverse.data

import com.levp.getdatabynet.currencyconverse.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "5fc4df8544f438f9b312f8ff3789d3bf"

interface CurrencyApi {

    @GET("/latest?access_key=$API_KEY")
    suspend fun getRates(
        @Query("base") base: String
    ): Response<CurrencyResponse>

    @GET("/latest?access_key=$API_KEY")
    suspend fun getTry(): Response<CurrencyResponse>

}