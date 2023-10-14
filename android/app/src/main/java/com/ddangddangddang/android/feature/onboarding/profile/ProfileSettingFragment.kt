package com.ddangddangddang.android.feature.onboarding.profile

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentProfileSettingBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.onboarding.OnBoardingViewModel
import com.ddangddangddang.android.util.binding.BindingFragment
import com.ddangddangddang.android.util.view.observeLoadingWithDialog
import com.ddangddangddang.android.util.view.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingFragment :
    BindingFragment<FragmentProfileSettingBinding>(R.layout.fragment_profile_setting) {
    private val activityViewModel: OnBoardingViewModel by activityViewModels()
    private val viewModel: ProfileSettingViewModel by viewModels()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) viewModel.setProfileImageUri(uri)
        }

    private val defaultUri by lazy {
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.img_default_profile))
            .appendPath(resources.getResourceTypeName(R.drawable.img_default_profile))
            .appendPath(resources.getResourceEntryName(R.drawable.img_default_profile))
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        requireContext().observeLoadingWithDialog(viewLifecycleOwner, viewModel.isLoading)
        if (viewModel.profile.value == null) viewModel.setupProfile(defaultUri)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.event.observe(viewLifecycleOwner) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: ProfileSettingViewModel.Event) {
        when (event) {
            is ProfileSettingViewModel.Event.FailureInitSetupProfileEvent -> {
                notifyProfileInitLoadFailed(event.errorType)
            }

            is ProfileSettingViewModel.Event.FailureChangeProfileEvent -> {
                notifyProfileChangeFailed(event.errorType)
            }

            ProfileSettingViewModel.Event.NavigateToNext -> {
                activityViewModel.nextPage()
            }

            ProfileSettingViewModel.Event.NavigateToSelectProfileImage -> {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }

    private fun notifyProfileInitLoadFailed(type: ErrorType) {
        val defaultMessage = getString(R.string.mypage_snackbar_load_profile_failed_title)
        val actionMessage = getString(R.string.all_snackbar_default_action)
        binding.root.showSnackbar(
            message = type.message ?: defaultMessage,
            actionMessage = actionMessage,
        )
    }

    private fun notifyProfileChangeFailed(type: ErrorType) {
        val defaultMessage = getString(R.string.profile_change_failed)
        val actionMessage = getString(R.string.all_snackbar_default_action)
        binding.root.showSnackbar(
            message = type.message ?: defaultMessage,
            actionMessage = actionMessage,
        )
    }
}
