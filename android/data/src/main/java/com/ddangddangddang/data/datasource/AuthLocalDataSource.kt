package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.local.AuthSharedPreference
import com.ddangddangddang.data.model.response.TokenResponse

class AuthLocalDataSource(private val sharedPreferences: AuthSharedPreference) {
    fun saveToken(token: TokenResponse) {
        sharedPreferences.accessToken = token.accessToken
        sharedPreferences.refreshToken = token.refreshToken
    }

    fun getAccessToken(): String = sharedPreferences.accessToken

    fun getRefreshToken(): String = sharedPreferences.refreshToken
}
