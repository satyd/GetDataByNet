package com.levp.getdatabynet.questionApi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.levp.getdatabynet.R
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@DelicateCoroutinesApi
class QuestionsActivity : AppCompatActivity() {

    private val BASE_URL_QUESTION = "https://jservice.io"
    private val NOT_LOADED_STR = "not loaded yet"
    val gson = GsonBuilder().setLenient().create()

    var currQuestion : String? = null
    var currAnswer : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        GlobalScope.launch(Dispatchers.Main) {
            getQuestion()
            question_text.text = currQuestion
        }

        next_question_btn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                getQuestion()
                question_text.text = currQuestion
                question_answer_text.text = "show answer"
            }



        }
        question_answer_text.setOnClickListener {
            question_answer_text.text = currAnswer
        }


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