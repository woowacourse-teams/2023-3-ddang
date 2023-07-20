package com.ddangddangddang.data.remote

import com.ddangddangddang.data.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AuctionRetrofit private constructor(retrofit: Retrofit) {
    val service: Service = retrofit.create(Service::class.java)

    companion object {
        private val HTTP_LOG_TAG = "HTTP_LOG"

        @Volatile
        private var instance: AuctionRetrofit? = null

        fun getInstance(): AuctionRetrofit {
            return instance ?: synchronized(this) {
                instance ?: createInstance()
            }
        }

        private fun createInstance(): AuctionRetrofit {
            return AuctionRetrofit(
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(createOkHttpClient())
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
                android.util.Log.e(HTTP_LOG_TAG, message)
            }
            return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
}
