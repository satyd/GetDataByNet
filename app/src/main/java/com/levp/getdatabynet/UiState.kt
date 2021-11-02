package com.levp.getdatabynet

import com.levp.getdatabynet.catApi.CatImageUrl

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val catImage: CatImageUrl
    ) : UiState()

    data class Error(val message: String) : UiState()
}