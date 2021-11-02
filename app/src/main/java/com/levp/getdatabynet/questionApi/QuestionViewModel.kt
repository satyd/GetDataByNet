package com.levp.getdatabynet.questionApi

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class QuestionViewModel(
    private val BASE_URL_QUESTION: String = "https://jservice.io",
    private val api: QuestionApi = buildQuestionApi(BASE_URL_QUESTION)
): QuestionBaseViewModel<UiState>() {

    fun getQuestion() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {

                val question = api.getQuestion()
                uiState.value = UiState.Success(question)


            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

}