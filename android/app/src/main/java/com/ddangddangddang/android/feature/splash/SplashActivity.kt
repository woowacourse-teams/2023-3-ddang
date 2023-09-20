package com.ddangddangddang.android.feature.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySplashBinding
import com.ddangddangddang.android.feature.login.LoginActivity
import com.ddangddangddang.android.feature.main.MainActivity
import com.ddangddangddang.android.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()
        viewModel.checkTokenExist()
    }

    private fun setupObserve() {
        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun handleEvent(event: SplashViewModel.SplashEvent) {
        when (event) {
            SplashViewModel.SplashEvent.AutoLoginSuccess -> navigateToMain()
            SplashViewModel.SplashEvent.RefreshTokenExpired -> navigateToLogin()
            SplashViewModel.SplashEvent.TokenNotExist -> navigateToLogin()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
