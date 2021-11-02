package com.levp.getdatabynet.questionApi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.inflate
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.levp.getdatabynet.R
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class QuestionsActivity : AppCompatActivity() {

    private val BASE_URL_QUESTION = "https://jservice.io"
    private val NOT_LOADED_STR = "not loaded yet"
    val gson = GsonBuilder().setLenient().create()
    private val viewModel: QuestionViewModel by viewModels()
    var currQuestion : String? = null
    var currAnswer : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        viewModel.getQuestion()

        next_question_btn.setOnClickListener {
            viewModel.getQuestion()
        }
        question_answer_text.setOnClickListener {
            question_answer_text.text = currAnswer
        }
    }
    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                onLoad()
            }
            is UiState.Success -> {
                onSuccess(uiState)
            }
            is UiState.Error -> {
                onError(uiState)
            }
        }
    }
    private fun onLoad()  {
        progressBar.visibility = View.VISIBLE
        next_question_btn.isEnabled = false
        question_answer_text.text = "show answer"

    }
    private fun onSuccess(uiState: UiState.Success){
        next_question_btn.isEnabled = true
        progressBar.visibility = View.GONE

        val response = uiState.question
        val list = response.body()
        val answer = list?.get(0)?.answer ?: NOT_LOADED_STR
        val question = list?.get(0)?.question ?: NOT_LOADED_STR

        question_text.text = question
        currAnswer = answer
    }
    private fun onError(uiState: UiState.Error) {
        progressBar.visibility = View.GONE
        next_question_btn.isEnabled = true

        toast(uiState.message)

    }


    suspend fun getQuestion(){
        var question = CompletableDeferred<String>()
        var answer = CompletableDeferred<String>()
        //var question = CompletableDeferred<String>()

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL_QUESTION)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(QuestionApi::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val num = 1
            Log.d("launch", "coroutine launched")
            val response = api.getQuestion()

            if (response.isSuccessful) {
                val list = response.body()
                answer.complete(list?.get(0)?.answer ?: NOT_LOADED_STR)
                question.complete(list?.get(0)?.question ?: NOT_LOADED_STR)


            } else {
                Log.e("response", "failed to load data")
            }
//            api.getComments().enque(object : Callback<List<Comment>> {
//                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
//                    Log.e("response", "error occured")
//                }
//
//                override fun onResponse(
//                    call: Call<List<Comment>>,
//                    response: Response<List<Comment>>
//                ) {
//                    response.body()?.let {
//                        for (comment in it) {
//                            Log.d("response", comment.toString)
//                        }
//                    }
//                }
//            })
        }
        currAnswer = answer.await()
        currQuestion = question.await()
        Log.e("question", "Question:$currQuestion\nAnswer:$currAnswer")

    }
}