package com.levp.getdatabynet.questionApi

import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface QuestionApi {
    @GET("/api/random")
    suspend fun getQuestions(@Query("count") count: Int): Response<List<Question>>

    @GET("/api/random?count=1")
    suspend fun getQuestion(): Response<List<Question>>
    //
}