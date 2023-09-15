package com.ddangddangddang.android.feature.profile

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityProfileChangeBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.compat.getParcelableCompat

class ProfileChangeActivity :
    BindingActivity<ActivityProfileChangeBinding>(R.layout.activity_profile_change) {
    private val viewModel by viewModels<ProfileChangeViewModel> { viewModelFactory }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        val profileModel =
            intent.getParcelableCompat<ProfileModel>(PROFILE_MODEL_KEY) ?: return finish()
        if (viewModel.profile.value == null) viewModel.setupProfile(profileModel, defaultUri)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun handleEvent(event: ProfileChangeViewModel.Event) {
        when (event) {
            ProfileChangeViewModel.Event.Exit -> finish()
            ProfileChangeViewModel.Event.NavigateToSelectProfileImage -> {
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }

            is ProfileChangeViewModel.Event.SuccessProfileChange -> {
                intent.putExtra(PROFILE_RESULT, event.profileModel)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    companion object {
        private const val PROFILE_MODEL_KEY = "profile_model_key"
        const val PROFILE_RESULT = "profile_result"

        fun getIntent(context: Context, profileModel: ProfileModel): Intent =
            Intent(context, ProfileChangeActivity::class.java).apply {
                putExtra(PROFILE_MODEL_KEY, profileModel)
            }
    }
}
