package com.levp.getdatabynet.numFactApi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class NumFactBaseViewModel<T> : ViewModel() {
    fun uiStateNF(): LiveData<T> = uiState
    protected val uiState: MutableLiveData<T> = MutableLiveData()
}
