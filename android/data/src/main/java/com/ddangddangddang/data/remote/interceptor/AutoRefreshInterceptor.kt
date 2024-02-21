package com.ddangddangddang.data.remote.interceptor

import com.ddangddangddang.data.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request

class AutoRefreshInterceptor {

    companion object {
        private const val AUTHORIZATION = "Authorization"

        fun createInstance(authRepository: AuthRepository) =
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
                .addHeader(AUTHORIZATION, token)
                .build()
        }
    }
}
