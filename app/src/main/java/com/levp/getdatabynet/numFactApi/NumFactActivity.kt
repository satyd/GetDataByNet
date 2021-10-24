package com.levp.getdatabynet.numFactApi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.levp.getdatabynet.R
import kotlinx.android.synthetic.main.activity_numfacts.*

import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@DelicateCoroutinesApi
class NumFactActivity : AppCompatActivity() {

    private val BASE_URL_NUMBER_FACT = "http://numbersapi.com"
    private val NOT_LOADED_STR = "not loaded yet"
    val gson = GsonBuilder().setLenient().create()

    var currText : String? = null
    var currNum : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numfacts)

        GlobalScope.launch(Dispatchers.Main) {
            getRandomFact()
            num_fact_TW.text = currText
        }

        next_numfact_btn.setOnClickListener {

            val currNum = num_facts_ET.text.toString().toLongOrNull()

            GlobalScope.launch(Dispatchers.Main) {
                getFact((currNum ?: 0L))
                num_fact_TW.text = currText

            }
        }

    }
    suspend fun getFact(num : Long){

        val fact = CompletableDeferred<String>()
        val number = CompletableDeferred<Long>()


        val api = Retrofit.Builder()
            .baseUrl(BASE_URL_NUMBER_FACT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NumFactApi::class.java)

        GlobalScope.launch(Dispatchers.IO) {

            Log.d("launch", "num coroutine launched")
            val response = api.getByValue(num)

            if (response.isSuccessful) {
                val ans = response.body()
                fact.complete(ans?.text ?: NOT_LOADED_STR)
                number.complete(ans?.number ?: 404)
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
        currText = fact.await()
        currNum = number.await()

        Log.e("number fact", "number:$currNum\nfact:$currText")

    }
    suspend fun getRandomFact(){

        val fact = CompletableDeferred<String>()
        val number = CompletableDeferred<Long>()


        val api = Retrofit.Builder()
            .baseUrl(BASE_URL_NUMBER_FACT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NumFactApi::class.java)

        GlobalScope.launch(Dispatchers.IO) {

            Log.d("launch", "num coroutine launched")
            val response = api.getRandom()

            if (response.isSuccessful) {
                val ans = response.body()
                fact.complete(ans?.text ?: NOT_LOADED_STR)
                number.complete(ans?.number ?: 404)
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
        currText = fact.await()
        currNum = number.await()

        Log.e("number fact", "number:$currNum\nfact:$currText")

    }
}