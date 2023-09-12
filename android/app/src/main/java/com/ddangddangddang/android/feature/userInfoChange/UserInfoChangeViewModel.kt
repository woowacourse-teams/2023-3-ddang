package com.ddangddangddang.android.feature.userInfoChange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.data.repository.UserRepository

class UserInfoChangeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _profile: MutableLiveData<ProfileModel> = MutableLiveData()
    val profile: LiveData<ProfileModel>
        get() = _profile

    fun setExitEvent() {
    }

    fun selectProfileImage() {
    }
}
