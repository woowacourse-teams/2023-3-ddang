package com.ddangddangddang.android.feature.mypage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.BuildConfig
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentMyPageBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.login.LoginActivity
import com.ddangddangddang.android.feature.userInfoChange.UserInfoChangeActivity
import com.ddangddangddang.android.util.binding.BindingFragment
import com.ddangddangddang.android.util.view.Toaster
import com.ddangddangddang.android.util.view.showSnackbar

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupObserve()
        if (viewModel.profile.value == null) viewModel.loadProfile()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) viewModel.loadProfile()
    }

    private fun setupObserve() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
    }

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) {
        when (event) {
            MyPageViewModel.MyPageEvent.LogoutSuccessfully -> {
                notifyLogoutSuccessfully()
                navigateToLogin()
            }

            MyPageViewModel.MyPageEvent.UserInfoChange -> {
                viewModel.profile.value?.let {
                    startActivity(UserInfoChangeActivity.getIntent(requireContext(), it))
                }
            }

            MyPageViewModel.MyPageEvent.ShowMyAuctions -> {
            }

            MyPageViewModel.MyPageEvent.ShowMyParticipateAuctions -> {
            }

            MyPageViewModel.MyPageEvent.ShowAnnouncement -> {
            }

            MyPageViewModel.MyPageEvent.ShowPrivacyPolicy -> showPrivacyPolicy()
            MyPageViewModel.MyPageEvent.LogoutFailed -> notifyLogoutFailed()
        }
    }

    private fun notifyLogoutSuccessfully() {
        Toaster.showShort(
            requireContext(),
            getString(R.string.mypage_toast_logout_successfully_message),
        )
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = FLAG_ACTIVITY_CLEAR_TASK + FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun notifyLogoutFailed() {
        binding.root.showSnackbar(R.string.mypage_snackbar_logout_failed_title)
    }

    private fun showPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVACY_POLICY_URL))
        startActivity(intent)
    }
}
