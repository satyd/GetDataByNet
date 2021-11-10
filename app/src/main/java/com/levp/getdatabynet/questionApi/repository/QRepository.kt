package com.levp.getdatabynet.questionApi.repository

import com.levp.getdatabynet.questionApi.QResource
import com.levp.getdatabynet.questionApi.data.Question


interface QRepository {
    suspend fun getQuestion(): QResource<List<Question>>
}