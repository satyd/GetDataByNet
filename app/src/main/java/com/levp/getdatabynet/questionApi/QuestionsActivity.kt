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


    private val NOT_LOADED_STR = "not loaded yet"

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
        currQuestion = question

    }
    private fun onError(uiState: UiState.Error) {
        progressBar.visibility = View.GONE
        next_question_btn.isEnabled = true
        toast(uiState.message)
    }
}