package com.levp.getdatabynet.questionApi.di

import com.google.gson.GsonBuilder
import com.levp.getdatabynet.questionApi.repository.DefaultQuestionRep
import com.levp.getdatabynet.questionApi.repository.QRepository
import com.levp.getdatabynet.questionApi.data.QuestionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL_QUESTION: String = "https://jservice.io"

@Module
@InstallIn(SingletonComponent::class)
object QuestionModule {

    @Singleton
    @Provides
    fun provideQuestionsApi(): QuestionApi = Retrofit.Builder()
        .baseUrl(BASE_URL_QUESTION)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
        .create(QuestionApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: QuestionApi): QRepository = DefaultQuestionRep(api)
}