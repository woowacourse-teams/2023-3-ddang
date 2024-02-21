package com.ddangddangddang.data.remote

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class LoggingInterceptor {

    companion object {
        private val HTTP_LOG_TAG = "HTTP_LOG"

        fun createInstance(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor { message ->
                Log.e(HTTP_LOG_TAG, message)
            }
            return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
}
