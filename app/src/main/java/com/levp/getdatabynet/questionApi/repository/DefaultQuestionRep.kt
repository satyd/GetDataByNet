package com.levp.getdatabynet.questionApi.repository


import com.levp.getdatabynet.questionApi.QResource
import com.levp.getdatabynet.questionApi.data.Question
import com.levp.getdatabynet.questionApi.data.QuestionApi
import javax.inject.Inject

class DefaultQuestionRep @Inject constructor(
    private val api: QuestionApi
) : QRepository {

    override suspend fun getQuestion(): QResource<List<Question>> {
        return try {
            val response = api.getQuestion()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                QResource.Success(result)
            } else {
                QResource.Error(response.message())
            }
        } catch (e: Exception) {
            QResource.Error(e.message ?: "Error occured")
        }
    }

}