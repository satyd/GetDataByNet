package com.levp.getdatabynet.numFactApi

import retrofit2.Response

sealed class UiStateNF {
    object Loading : UiStateNF()
    data class Success(
        val numFactResponse: Response<NumFact>
    ) : UiStateNF()

    data class Error(val message: String) : UiStateNF()
}