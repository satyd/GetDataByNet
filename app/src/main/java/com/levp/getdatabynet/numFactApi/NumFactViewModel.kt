package com.levp.getdatabynet.numFactApi

import androidx.lifecycle.viewModelScope
import com.levp.getdatabynet.questionApi.buildNumFactApi
import com.levp.getdatabynet.questionApi.log
import kotlinx.android.synthetic.main.activity_numfacts.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*


class NumFactViewModel(
    private val BASE_URL_NUMFACT: String = "http://numbersapi.com",
    private val api: NumFactApi = buildNumFactApi(BASE_URL_NUMFACT)
): NumFactBaseViewModel<UiStateNF>() {

    val c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)

    fun getFact(num: Long, fType: String, date : String ) {
        uiState.value = UiStateNF.Loading

        viewModelScope.launch {
            val response: Response<NumFact> = when (fType) {
                "trivia" -> {
                    api.getByValueTrivia(num)
                }
                "math" -> {
                    api.getByValueMath(num)
                }
                "year" -> {
                    api.getByValueYear(num)
                }
                "date" -> {
                    //val txt = num_facts_ET.text.toString().split(".")
                    val txt = date.split(".")
                    api.getByDate(txt[1].toLong(), txt[0].toLong())
                }
                else -> {
                    api.getRandom()
                }
            }
            try {
                uiState.value = UiStateNF.Success(response)
            } catch (exception: Exception) {
                uiState.value = UiStateNF.Error("Network Request failed")
            }
        }
    }

    fun getRandomFact(fType:String, date :String = "$day.${month + 1}"){
        uiState.value = UiStateNF.Loading
        viewModelScope.launch {
            val response = if (fType != "date") {
                api.getRandomByType(fType)
            } else {
                val txt = date.split(".")
                api.getByDate(txt[1].toLong(), txt[0].toLong())
            }
            try {

                uiState.value = UiStateNF.Success(response)
            } catch (exception: Exception) {
                uiState.value = UiStateNF.Error("Network Request failed")
            }

        }
    }

}