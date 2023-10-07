package com.ddangddangddang.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class AuthSharedPreference(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences: SharedPreferences
    var accessToken: String
        get() = sharedPreferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
        set(value) {
            sharedPreferences
                .edit()
                .putString(KEY_ACCESS_TOKEN, value)
                .apply()
        }
    var refreshToken: String
        get() = sharedPreferences.getString(KEY_REFRESH_TOKEN, "") ?: ""
        set(value) {
            sharedPreferences
                .edit()
                .putString(KEY_REFRESH_TOKEN, value)
                .apply()
        }

    init {
        sharedPreferences = EncryptedSharedPreferences.create(
            FILE_NAME,
            masterKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    companion object {
        private const val FILE_NAME = "auth"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
