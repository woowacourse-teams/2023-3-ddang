package com.ddangddangddang.data.remote

import android.util.Log
import com.ddangddangddang.data.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AuthRetrofit private constructor(retrofit: Retrofit) {
    val service: AuthService = retrofit.create(AuthService::class.java)

    companion object {
        private val HTTP_LOG_TAG = "HTTP_LOG"

        @Volatile
        private var instance: AuthRetrofit? = null

        fun getInstance(): AuthRetrofit {
            return instance ?: synchronized(this) {
                instance ?: createInstance()
            }
        }

        private fun createInstance(): AuthRetrofit {
            return AuthRetrofit(
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(createOkHttpClient())
                    .addCallAdapterFactory(CallAdapterFactory())
                    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                    .build(),
            ).also { instance = it }
        }

        private fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().apply {
                addInterceptor(getHttpLoggingInterceptor())
            }.build()
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor { message ->
                Log.e(HTTP_LOG_TAG, message)
            }
            return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
}
