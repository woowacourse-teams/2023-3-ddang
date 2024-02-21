package com.ddangddangddang.android.feature.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.util.image.toAdjustImageFile
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.ProfileUpdateRequest
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileChangeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private lateinit var originalProfileUri: Uri

    private val _profile: MutableLiveData<Uri> = MutableLiveData()
    val profile: LiveData<Uri>
        get() = _profile

    val userNickname: MutableLiveData<String> = MutableLiveData()

    fun setupProfile(profileModel: ProfileModel, defaultUri: Uri) {
        val originProfileUri = profileModel.profileImage?.let { Uri.parse(it) } ?: defaultUri
        _profile.value = originProfileUri
        originalProfileUri = originProfileUri
        userNickname.value = profileModel.name
    }

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    fun selectProfileImage() {
        _event.value = Event.NavigateToSelectProfileImage
    }

    fun setProfileImageUri(uri: Uri) {
        _profile.value = uri
    }

    fun submitProfile(context: Context) {
        val name = userNickname.value?.trim() ?: return
        val profileImageUri = profile.value?.takeIf { it.path != originalProfileUri.path }
        viewModelScope.launch {
            val file = runCatching { profileImageUri?.toAdjustImageFile(context) }.getOrNull()
            when (val response = userRepository.updateProfile(file, ProfileUpdateRequest(name))) {
                is ApiResponse.Success -> {
                    _event.value = Event.SuccessProfileChange
                }

                is ApiResponse.Failure -> {
                    _event.value =
                        Event.FailureChangeProfileEvent(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = Event.FailureChangeProfileEvent(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = Event.FailureChangeProfileEvent(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    sealed class Event {
        object Exit : Event()
        object NavigateToSelectProfileImage : Event()
        object SuccessProfileChange : Event()
        data class FailureChangeProfileEvent(val errorType: ErrorType) : Event()
    }
}
