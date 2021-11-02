package com.levp.getdatabynet.questionApi

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun buildQuestionApi(BASE_URL_QUESTION: String): QuestionApi =
    Retrofit.Builder()
        .baseUrl(BASE_URL_QUESTION)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
        .create(QuestionApi::class.java)

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }