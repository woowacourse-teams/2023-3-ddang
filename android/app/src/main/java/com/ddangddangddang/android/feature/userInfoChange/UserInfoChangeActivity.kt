package com.ddangddangddang.android.feature.userInfoChange

import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityUserInfoChangeBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class UserInfoChangeActivity :
    BindingActivity<ActivityUserInfoChangeBinding>(R.layout.activity_user_info_change) {
    private val viewModel by viewModels<UserInfoChangeViewModel> { viewModelFactory }
}
