package com.levp.getdatabynet.questionApi

import retrofit2.Response

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val question: Question
    ) : UiState()

    data class Error(val message: String) : UiState()
}