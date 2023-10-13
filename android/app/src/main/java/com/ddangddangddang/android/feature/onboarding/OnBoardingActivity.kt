package com.ddangddangddang.android.feature.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityOnboardingBinding
import com.ddangddangddang.android.feature.main.MainActivity
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel: OnBoardingViewModel by viewModels()
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        onBackPressedDispatcher.addCallback(this, callback)
        setupOnBoardingFragmentAdapter()
        setupViewModel()
    }

    private fun setupOnBoardingFragmentAdapter() {
        binding.vpOnboarding.isUserInputEnabled = false
    }

    private fun setupViewModel() {
        viewModel.event.observe(this) {
            handleEvent(it)
        }
        viewModel.currentPageType.observe(this) {
            binding.vpOnboarding.currentItem = it.ordinal // 해당 페이지로 이동
        }
    }

    private fun handleEvent(event: OnBoardingViewModel.Event) {
        when (event) {
            OnBoardingViewModel.Event.Exit -> showExitDialog()
            OnBoardingViewModel.Event.CompleteExit -> navigateToMain()
        }
    }

    private fun showExitDialog() {
        showDialog(
            titleId = R.string.onboarding_page_exit_dialog_title,
            messageId = R.string.onboarding_page_exit_dialog_message,
            positiveStringId = R.string.onboarding_page_exit_dialog_positive_button,
            actionPositive = {
                navigateToMain()
            },
            isCancelable = false,
        )
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
