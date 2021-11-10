package com.levp.getdatabynet.questionApi



sealed class QResource <T>(val data: T?, val message: String?) {
    class Success<T>(data: T) : QResource<T>(data, null)
    class Error<T>(message: String) : QResource<T>(null, message)
}