package com.ddangddangddang.data.remote.client

import okhttp3.Interceptor
import okhttp3.OkHttpClient

class AutoRefreshHttpClient {

    companion object {
        fun createInstance(
            loggingInterceptor: Interceptor,
            autoRefreshInterceptor: Interceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder().apply {
                addInterceptor(loggingInterceptor)
                addInterceptor(autoRefreshInterceptor)
            }.build()
        }
    }
}
