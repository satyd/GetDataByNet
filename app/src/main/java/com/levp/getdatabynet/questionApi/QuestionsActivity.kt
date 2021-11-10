package com.levp.getdatabynet.questionApi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.levp.getdatabynet.R
import com.levp.getdatabynet.questionApi.repository.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_questions.*

@AndroidEntryPoint
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

        val answer = response.answer ?: NOT_LOADED_STR
        val question = response.question ?: NOT_LOADED_STR

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