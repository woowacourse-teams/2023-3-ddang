package com.ddangddangddang.data.remote.retrofit

import com.ddangddangddang.data.remote.callAdapter.CallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class RetrofitFactory {

    companion object {
        fun createInstance(serverURL: String, httpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(serverURL)
                .client(httpClient)
                .addCallAdapterFactory(CallAdapterFactory())
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }
    }
}
