package com.levp.getdatabynet

import android.app.Application
import com.levp.getdatabynet.questionApi.QuestionViewModel
import dagger.hilt.android.HiltAndroidApp

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.inject.Inject
import javax.net.ssl.SSLContext

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}