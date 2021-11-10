package com.levp.getdatabynet.questionApi

import com.levp.getdatabynet.questionApi.data.Question

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val question: Question
    ) : UiState()

    data class Error(val message: String) : UiState()
}