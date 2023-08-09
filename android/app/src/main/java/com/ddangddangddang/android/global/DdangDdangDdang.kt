package com.ddangddangddang.android.global

import android.app.Application
import com.ddangddangddang.android.BuildConfig
import com.ddangddangddang.data.remote.AuctionRetrofit
import com.ddangddangddang.data.repository.AuthRepositoryImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.kakao.sdk.common.KakaoSdk
import kotlin.NullPointerException

class DdangDdangDdang : Application() {
    override fun onCreate() {
        super.onCreate()
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        _authRepository =
            AuthRepositoryImpl.getInstance(this, AuctionRetrofit.getInstance().service)

        KakaoSdk.init(this, BuildConfig.KEY_KAKAO)
    }

    companion object {
        private var _firebaseAnalytics: FirebaseAnalytics? = null
        val firebaseAnalytics: FirebaseAnalytics?
            get() = _firebaseAnalytics
        private var _authRepository: AuthRepositoryImpl? = null
        val authRepository: AuthRepositoryImpl
            get() = _authRepository ?: throw NullPointerException("AuthRepository가 존재하지 않습니다.")
    }
}
