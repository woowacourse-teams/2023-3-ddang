package com.ddangddangddang.android.feature.mypage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
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

            MyPageViewModel.MyPageEvent.NavigateToAnnouncement -> {
            }

            MyPageViewModel.MyPageEvent.NavigateToPrivacyPolicy -> showPrivacyPolicy()
            MyPageViewModel.MyPageEvent.LogoutSuccessfully -> {
                notifyLogoutSuccessfully()
                navigateToLogin()
            }

            is MyPageViewModel.MyPageEvent.LogoutFailed -> {
                val defaultMessage = getString(R.string.mypage_snackbar_logout_failed_title)
                notifyRequestFailed(event.type, defaultMessage)
            }

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

    private fun showPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVACY_POLICY_URL))
        startActivity(intent)
    }

    private fun notifyWithdrawalSuccessfully() {
        Toaster.showShort(
            requireContext(),
            getString(R.string.mypage_toast_withdrawal_successfully_message),
        )
    }

    private fun notifyRequestFailed(type: ErrorType, defaultMessage: String) {
        val actionMessage = getString(R.string.all_snackbar_default_action)
        val message = when (type) {
            is ErrorType.FAILURE -> type.message
            is ErrorType.NETWORK_ERROR -> getString(type.messageId)
            is ErrorType.UNEXPECTED -> getString(type.messageId)
        }
        binding.root.showSnackbar(
            message = message ?: defaultMessage,
            actionMessage = actionMessage,
        )
    }
}
