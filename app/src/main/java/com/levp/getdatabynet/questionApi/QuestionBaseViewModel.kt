package com.levp.getdatabynet.questionApi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

open class QuestionBaseViewModel<T> : ViewModel() {

    fun uiState(): LiveData<T> = uiState
    protected val uiState: MutableLiveData<T> = MutableLiveData()
}
