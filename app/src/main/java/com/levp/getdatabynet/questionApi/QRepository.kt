package com.levp.getdatabynet.questionApi

import com.levp.getdatabynet.questionApi.QResource
import com.levp.getdatabynet.questionApi.Question
import retrofit2.Response


interface QRepository {
    suspend fun getQuestion(): QResource<List<Question>>
}