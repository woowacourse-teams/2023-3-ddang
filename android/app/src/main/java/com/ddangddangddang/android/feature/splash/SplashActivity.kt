package com.ddangddangddang.android.feature.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySplashBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.login.LoginActivity
import com.ddangddangddang.android.feature.main.MainActivity
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.Toaster
import com.ddangddangddang.android.util.view.showDialog
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels()

    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                requestUpdate()
            } else {
                viewModel.checkTokenExist()
            }
        }.addOnFailureListener {
            viewModel.checkTokenExist()
        }
    }

    private fun setupObserve() {
        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun handleEvent(event: SplashViewModel.SplashEvent) {
        when (event) {
            SplashViewModel.SplashEvent.AutoLoginSuccess -> navigateToMain()
            SplashViewModel.SplashEvent.RefreshTokenExpired -> navigateToLogin()
            SplashViewModel.SplashEvent.TokenNotExist -> navigateToLogin()
            is SplashViewModel.SplashEvent.FailureStartDdangDdangDdang -> {
                showErrorMessage(event.errorType)
                finish()
            }
        }
    }

    private fun navigateToMain() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(LoginActivity.getIntent(this))
        finish()
    }

    private fun showErrorMessage(errorType: ErrorType) {
        Toaster.showShort(
            this,
            errorType.message ?: getString(R.string.splash_app_default_error_message),
        )
    }

    private fun requestUpdate() {
        showDialog(
            titleId = R.string.splash_app_update_request_dialog_title,
            messageId = R.string.splash_app_update_request_dialog_message,
            negativeStringId = R.string.all_dialog_default_negative_button,
            positiveStringId = R.string.all_dialog_default_positive_button,
            actionPositive = {
                navigateToPlayStore()
                finish()
            },
            actionNegative = {
                Toaster.showShort(this, getString(R.string.splash_app_update_denied))
                finish()
            },
            isCancelable = false,
        )
    }

    private fun navigateToPlayStore() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    }
}
