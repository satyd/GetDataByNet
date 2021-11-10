package com.levp.getdatabynet.currencyconverse.repository


import com.levp.getdatabynet.currencyconverse.data.models.CurrencyResponse
import com.levp.getdatabynet.currencyconverse.util.Resource

interface MainRepositoryCC {
    suspend fun getRates(base: String): Resource<CurrencyResponse>
    suspend fun getTry(): Resource<CurrencyResponse>
}