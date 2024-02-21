package com.ddangddangddang.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient

class DefaultHttpClient {

    companion object {
        fun createOkHttpClient(LoggingInterceptor: Interceptor): OkHttpClient {
            return OkHttpClient.Builder().apply {
                addInterceptor(LoggingInterceptor)
            }.build()
        }
    }
}
