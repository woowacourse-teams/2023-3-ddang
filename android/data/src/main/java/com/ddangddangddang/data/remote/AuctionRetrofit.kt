package com.ddangddangddang.data.remote

import com.ddangddangddang.data.BuildConfig
import com.ddangddangddang.data.repository.AuthRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AuctionRetrofit private constructor(retrofit: Retrofit) {
    val service: AuctionService = retrofit.create(AuctionService::class.java)

    companion object {
        private const val HTTP_LOG_TAG = "HTTP_LOG"
        private const val AUTHORIZATION = "Authorization"

        @Volatile
        private var instance: AuctionRetrofit? = null

        fun getInstance(authRepository: AuthRepository): AuctionRetrofit {
            return instance ?: synchronized(this) {
                instance ?: createInstance(authRepository)
            }
        }

        private fun createInstance(authRepository: AuthRepository): AuctionRetrofit {
            return AuctionRetrofit(
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(createOkHttpClient(authRepository))
                    .addCallAdapterFactory(CallAdapterFactory())
                    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                    .build(),
            ).also { instance = it }
        }

        private fun createOkHttpClient(authRepository: AuthRepository): OkHttpClient {
            return OkHttpClient.Builder().apply {
                addInterceptor(getHttpLoggingInterceptor())
                addInterceptor(getAuthInterceptor(authRepository))
            }.build()
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor { message ->
                android.util.Log.e(HTTP_LOG_TAG, message)
            }
            return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        private fun getAuthInterceptor(authRepository: AuthRepository) =
            Interceptor { chain: Interceptor.Chain ->
                val tokenAddedRequest =
                    chain.request().putTokenHeader(authRepository.getAccessToken())

                val response = chain.proceed(tokenAddedRequest)

                if (response.code == 401) {
                    response.close()
                    refreshTokenRequest(authRepository)

                    val refreshedTokenAddedRequest =
                        chain.request().putTokenHeader(authRepository.getAccessToken())

                    return@Interceptor chain.proceed(refreshedTokenAddedRequest)
                }

                return@Interceptor response
            }

        private fun refreshTokenRequest(authRepository: AuthRepository) =
            runBlocking {
                authRepository.refreshToken()
            }

        private fun Request.putTokenHeader(token: String): Request {
            return this.newBuilder()
                .addHeader(AUTHORIZATION, "Bearer $token")
                .build()
        }
    }
}
