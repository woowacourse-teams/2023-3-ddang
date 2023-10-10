package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.local.AuthSharedPreference
import com.ddangddangddang.data.model.response.TokenResponse
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(private val sharedPreferences: AuthSharedPreference) {
    fun saveToken(token: TokenResponse) {
        sharedPreferences.accessToken = token.accessToken
        sharedPreferences.refreshToken = token.refreshToken
    }

    fun getAccessToken(): String = sharedPreferences.accessToken

    fun getRefreshToken(): String = sharedPreferences.refreshToken
}
