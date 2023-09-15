package com.ddangddangddang.android.feature.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityLoginBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.main.MainActivity
import com.ddangddangddang.android.global.AnalyticsDelegate
import com.ddangddangddang.android.global.AnalyticsDelegateImpl
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.showSnackbar
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity :
    BindingActivity<ActivityLoginBinding>(R.layout.activity_login),
    AnalyticsDelegate by AnalyticsDelegateImpl() {
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAnalytics(javaClass.simpleName, lifecycle)

        binding.viewModel = viewModel
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.event.observe(this) {
            when (it) {
                is LoginViewModel.LoginEvent.KakaoLoginEvent -> loginByKakao()
                is LoginViewModel.LoginEvent.CompleteLoginEvent -> navigateToMain()
                is LoginViewModel.LoginEvent.FailureLoginEvent -> notifyLoginFailed(it.message)
            }
        }
    }

    private fun loginByKakao() {
        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) return loginByKakaoTalk()
        loginByKakaoAccount()
    }

    private fun loginByKakaoTalk() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                if (isClientCancelError(error)) {
                    logLoginError(error)
                    return@loginWithKakaoTalk
                }
                loginByKakaoAccount()
            } else if (token != null) {
                completeLoginByKakao(token)
            }
        }
    }

    private fun isClientCancelError(error: Throwable?) =
        error is ClientError && error.reason == ClientErrorCause.Cancelled

    private fun loginByKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                logLoginError(error)
            } else if (token != null) {
                completeLoginByKakao(token)
            }
        }
    }

    private fun logLoginError(error: Throwable) {
        Log.d("test", "로그인 실패 $error")
    }

    private fun completeLoginByKakao(token: OAuthToken) {
        viewModel.completeLoginByKakao(token.accessToken)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun notifyLoginFailed(message: String?) {
        val defaultMessage = getString(R.string.login_snackbar_login_failed_title)
        val actionMessage = getString(R.string.all_snackbar_default_action)
        binding.root.showSnackbar(message = message ?: defaultMessage, actionMessage = actionMessage)
    }
}
