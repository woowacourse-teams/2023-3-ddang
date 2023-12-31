package com.ddangddangddang.android.feature.mypage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import android.provider.Settings.EXTRA_APP_PACKAGE
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.BuildConfig
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentMyPageBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.login.LoginActivity
import com.ddangddangddang.android.feature.myAuction.MyAuctionActivity
import com.ddangddangddang.android.feature.participateAuction.ParticipateAuctionActivity
import com.ddangddangddang.android.feature.profile.ProfileChangeActivity
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.util.binding.BindingFragment
import com.ddangddangddang.android.util.view.Toaster
import com.ddangddangddang.android.util.view.observeLoadingWithDialog
import com.ddangddangddang.android.util.view.showDialog
import com.ddangddangddang.android.util.view.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()
    private val profileChangeActivityLauncher = setupChangeProfileLauncher()

    private fun setupChangeProfileLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                viewModel.loadProfile()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        if (viewModel.profile.value == null) viewModel.loadProfile()
        setupObserve()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) viewModel.loadProfile()
    }

    private fun setupObserve() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
        requireContext().observeLoadingWithDialog(viewLifecycleOwner, viewModel.isLoading)
    }

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) {
        when (event) {
            is MyPageViewModel.MyPageEvent.LoadProfileFailed -> {
                val defaultMessage = getString(R.string.mypage_snackbar_load_profile_failed_title)
                notifyRequestFailed(event.type, defaultMessage)
            }

            MyPageViewModel.MyPageEvent.ProfileChange -> {
                viewModel.profile.value?.let { navigateToUserInfoChange(it) }
            }

            MyPageViewModel.MyPageEvent.NavigateToMyAuctions -> {
                navigateToMyAuction()
            }

            MyPageViewModel.MyPageEvent.NavigateToMyParticipateAuctions -> {
                navigateToMyParticipateAuction()
            }

            MyPageViewModel.MyPageEvent.NavigateToNotificationSettings -> {
                navigateToNotificationSettings()
            }

            MyPageViewModel.MyPageEvent.NavigateToAnnouncement -> {
            }

            MyPageViewModel.MyPageEvent.ContactUs -> contactUs()

            MyPageViewModel.MyPageEvent.NavigateToPrivacyPolicy -> showPrivacyPolicy()

            MyPageViewModel.MyPageEvent.LogoutSuccessfully -> {
                notifyLogoutSuccessfully()
                navigateToLogin()
            }

            is MyPageViewModel.MyPageEvent.LogoutFailed -> {
                val defaultMessage = getString(R.string.mypage_snackbar_logout_failed_title)
                notifyRequestFailed(event.type, defaultMessage)
            }

            MyPageViewModel.MyPageEvent.AskWithdrawal -> askWithdrawal()

            MyPageViewModel.MyPageEvent.WithdrawalSuccessfully -> {
                notifyWithdrawalSuccessfully()
                navigateToLogin()
            }

            is MyPageViewModel.MyPageEvent.WithdrawalFailed -> {
                val defaultMessage = getString(R.string.mypage_snackbar_withdrawal_failed_title)
                notifyRequestFailed(event.type, defaultMessage)
            }
        }
    }

    private fun navigateToMyAuction() {
        startActivity(Intent(requireContext(), MyAuctionActivity::class.java))
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

    private fun navigateToUserInfoChange(profileModel: ProfileModel) {
        profileChangeActivityLauncher.launch(
            ProfileChangeActivity.getIntent(
                requireContext(),
                profileModel,
            ),
        )
    }

    private fun navigateToMyParticipateAuction() {
        startActivity(Intent(requireContext(), ParticipateAuctionActivity::class.java))
    }

    private fun navigateToNotificationSettings() {
        val intent = Intent(ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(EXTRA_APP_PACKAGE, requireContext().packageName)
        startActivity(intent)
    }

    private fun contactUs() {
        val address = BuildConfig.DDANG_EMAIL_ADDRESS
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$address")
        }
        startActivity(intent)
    }

    private fun showPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVACY_POLICY_URL))
        startActivity(intent)
    }

    private fun askWithdrawal() {
        showDialog(
            titleId = R.string.mypage_dialog_withdrawal_title,
            messageId = R.string.mypage_dialog_withdrawal_message,
            negativeStringId = R.string.all_dialog_default_negative_button,
            positiveStringId = R.string.mypage_dialog_withdrawal_positive_button,
            actionPositive = viewModel::withdrawal,
        )
    }

    private fun notifyWithdrawalSuccessfully() {
        Toaster.showShort(
            requireContext(),
            getString(R.string.mypage_toast_withdrawal_successfully_message),
        )
    }

    private fun notifyRequestFailed(type: ErrorType, defaultMessage: String) {
        val actionMessage = getString(R.string.all_snackbar_default_action)
        binding.root.showSnackbar(
            message = type.message ?: defaultMessage,
            actionMessage = actionMessage,
        )
    }
}
