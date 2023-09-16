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
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.login.LoginActivity
import com.ddangddangddang.android.feature.profile.ProfileChangeActivity
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.util.binding.BindingFragment
import com.ddangddangddang.android.util.compat.getParcelableCompat
import com.ddangddangddang.android.util.view.Toaster
import com.ddangddangddang.android.util.view.observeLoadingWithDialog
import com.ddangddangddang.android.util.view.showSnackbar

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels { viewModelFactory }
    private val profileChangeActivityLauncher = setupChangeProfileLauncher()

    private fun setupChangeProfileLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val profile =
                    it.data?.getParcelableCompat<ProfileModel>(ProfileChangeActivity.PROFILE_RESULT)
                        ?: return@registerForActivityResult
                viewModel.updateProfile(profile)
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
            MyPageViewModel.MyPageEvent.LogoutSuccessfully -> {
                notifyLogoutSuccessfully()
                navigateToLogin()
            }

            MyPageViewModel.MyPageEvent.ProfileChange -> {
                viewModel.profile.value?.let { navigateToUserInfoChange(it) }
            }

            MyPageViewModel.MyPageEvent.NavigateToMyAuctions -> {
            }

            MyPageViewModel.MyPageEvent.NavigateToMyParticipateAuctions -> {
            }

            MyPageViewModel.MyPageEvent.NavigateToAnnouncement -> {
            }

            MyPageViewModel.MyPageEvent.NavigateToPrivacyPolicy -> showPrivacyPolicy()
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

    private fun navigateToUserInfoChange(profileModel: ProfileModel) {
        profileChangeActivityLauncher.launch(
            ProfileChangeActivity.getIntent(
                requireContext(),
                profileModel,
            ),
        )
    }

    private fun notifyLogoutFailed() {
        binding.root.showSnackbar(R.string.mypage_snackbar_logout_failed_title)
    }

    private fun showPrivacyPolicy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVACY_POLICY_URL))
        startActivity(intent)
    }
}
