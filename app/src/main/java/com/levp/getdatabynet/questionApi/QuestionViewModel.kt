package com.levp.getdatabynet.questionApi

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val repository: QRepository
) : QuestionBaseViewModel<UiState>() {

    fun getQuestion() {
        uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = repository.getQuestion()) {
                is QResource.Success -> {
                    val question = response.data!![0]
                    uiState.postValue(UiState.Success(question))
                }
                is QResource.Error -> uiState.postValue(UiState.Error("Network Request failed"))
            }
        }
    }
}