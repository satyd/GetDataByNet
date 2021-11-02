package com.levp.getdatabynet.questionApi

import retrofit2.Response

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val question: Response<List<Question>>
    ) : UiState()

    data class Error(val message: String) : UiState()
}