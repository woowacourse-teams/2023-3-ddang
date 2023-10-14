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
        override fun handleOnBackPressed() {
            viewModel.previousPage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        onBackPressedDispatcher.addCallback(this, callback)
        setupOnBoardingFragmentAdapter()
        setupViewModel()
    }

    private fun setupOnBoardingFragmentAdapter() {
        binding.vpOnboarding.adapter = OnBoardingFragmentAdapter(this)
        binding.vpOnboarding.isUserInputEnabled = false // 유저가 직접 스와이프 이동 못하게 막기.
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
            OnBoardingViewModel.Event.Skip -> showSkipDialog() // 건너 뛰기 버튼을 누르는 경우
            OnBoardingViewModel.Event.Exit -> navigateToMain() // 모든 과정을 완료했을때
        }
    }

    private fun showSkipDialog() {
        showDialog(
            titleId = R.string.onboarding_page_skip_dialog_title,
            messageId = R.string.onboarding_page_skip_dialog_message,
            positiveStringId = R.string.onboarding_page_skip_dialog_positive_button,
            negativeStringId = R.string.all_dialog_default_negative_button,
            actionPositive = { navigateToMain() },
            isCancelable = false,
        )
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
