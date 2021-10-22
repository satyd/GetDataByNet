package com.levp.getdatabynet

import android.app.Application

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //tried to run on API 19
    }
}