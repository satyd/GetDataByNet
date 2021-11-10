package com.levp.getdatabynet.currencyconverse.repository

import com.levp.getdatabynet.currencyconverse.data.CurrencyApi
import com.levp.getdatabynet.currencyconverse.data.models.CurrencyResponse
import com.levp.getdatabynet.currencyconverse.util.Resource
import javax.inject.Inject

class DefaultMainRepository@Inject constructor(
    private val api: CurrencyApi
) : MainRepositoryCC {

    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch(e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }
    override suspend fun getTry(): Resource<CurrencyResponse> {
        return try {
            val response = api.getTry()
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch(e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }
}