package com.levp.getdatabynet.networkHabr

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkService {
    private var mRetrofit: Retrofit? = null

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
        var mInstance: NetworkService? = null

        fun getInstance(): NetworkService {
            if (mInstance == null) {
                mInstance = NetworkService()

            }
            return mInstance!!
        }

    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)

        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }

    private fun NetworkService() {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getJSONApi(): JSONPlaceHolderApi? {
        return mRetrofit!!.create(JSONPlaceHolderApi::class.java)
    }

}