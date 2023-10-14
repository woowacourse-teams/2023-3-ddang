package com.ddangddangddang.android.feature.onboarding.profile

import android.os.Bundle
import android.view.View
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentProfileSettingBinding
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingFragment :
    BindingFragment<FragmentProfileSettingBinding>(R.layout.fragment_profile_setting) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
