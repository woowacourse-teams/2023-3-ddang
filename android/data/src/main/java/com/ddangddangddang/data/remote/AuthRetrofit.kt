package com.ddangddangddang.data.remote

import com.ddangddangddang.data.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class AuthRetrofit {
    companion object {

        fun createInstance(httpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(CallAdapterFactory())
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }
    }
}
