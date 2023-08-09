package com.ddangddangddang.android.feature.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySplashBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
