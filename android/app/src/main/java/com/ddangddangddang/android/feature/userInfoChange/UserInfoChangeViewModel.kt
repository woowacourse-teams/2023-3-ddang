package com.ddangddangddang.android.feature.userInfoChange

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.ProfileChangeModel
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.repository.UserRepository

class UserInfoChangeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private val _profile: MutableLiveData<ProfileChangeModel> = MutableLiveData()
    val profile: LiveData<ProfileChangeModel>
        get() = _profile

    val userNickname: MutableLiveData<String> = MutableLiveData()

    fun setInitUserInfo(profileModel: ProfileModel, defaultUri: Uri) {
        _profile.value = ProfileChangeModel(
            profileModel.name,
            profileModel.profileImage?.let { Uri.parse(it) } ?: defaultUri,
            profileModel.reliability,
        )
        userNickname.value = profileModel.name
    }

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    fun selectProfileImage() {
        _event.value = Event.NavigateToSelectProfileImage
    }

    fun setImage(uri: Uri) {
        _profile.value = _profile.value?.copy(profileImage = uri)
    }

    fun confirm() {
    }

    sealed class Event {
        object Exit : Event()
        object NavigateToSelectProfileImage : Event()
    }
}
