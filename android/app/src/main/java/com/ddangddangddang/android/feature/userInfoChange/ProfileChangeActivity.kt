package com.ddangddangddang.android.feature.userInfoChange

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
    private val viewModel by viewModels<UserInfoChangeViewModel> { viewModelFactory }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) viewModel.setImage(uri)
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
        if (viewModel.profile.value == null) viewModel.setInitUserInfo(profileModel, defaultUri)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun handleEvent(event: UserInfoChangeViewModel.Event) {
        when (event) {
            UserInfoChangeViewModel.Event.Exit -> finish()
            UserInfoChangeViewModel.Event.NavigateToSelectProfileImage -> {
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }
        }
    }

    companion object {
        private const val PROFILE_MODEL_KEY = "profile_model_key"

        fun getIntent(context: Context, profileModel: ProfileModel): Intent =
            Intent(context, ProfileChangeActivity::class.java).apply {
                putExtra(PROFILE_MODEL_KEY, profileModel)
            }
    }
}
