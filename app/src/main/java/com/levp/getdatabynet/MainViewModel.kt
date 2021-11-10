package com.levp.getdatabynet

import androidx.lifecycle.viewModelScope
import com.levp.getdatabynet.catApi.CatApi
import com.levp.getdatabynet.catApi.CatImageUrl
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel() : BaseViewModel<UiState>() {

    private val CAT_URL = "https://aws.random.cat"
    private val api = Retrofit.Builder()
        .baseUrl(CAT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatApi::class.java)

    fun getCat() {
        uiState.value = UiState.Loading

        val catPicDeferred = viewModelScope.async { api.getCatPic() }

        viewModelScope.launch {
            try {
                val res = catPicDeferred.await()
                uiState.value =
                    UiState.Success(CatImageUrl(res.body()?.file ?: "https://http.cat/404"))

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }


}