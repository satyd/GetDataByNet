package com.levp.getdatabynet.numFactApi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

import retrofit2.http.Query

interface NumFactApi {
    @GET("/api/random")
    suspend fun getQuestions(@Query("count") count: Int): Response<List<NumFact>>

    @GET("/{num}?json")
    suspend fun getByValue(@Path("num") num : Long): Response<NumFact>

    @GET("/{num}/math")
    suspend fun getByValueMath(@Path("num") num : Long): Response<NumFact>

    @GET("/{num}/trivia")
    suspend fun getByValueTrivia(@Path("num") num : Long): Response<NumFact>

    @GET("/random?json")
    suspend fun getRandom(): Response<NumFact>

    @GET("/random")
    suspend fun getRandomInRange(@Query("min") min:Int, @Query("max") max:Int): Response<NumFact>
    //
}