package com.ddangddangddang.android.feature.onboarding.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.mapper.ProfileModelMapper.toPresentation
import com.ddangddangddang.android.util.image.toAdjustImageFile
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.ProfileUpdateRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private lateinit var currentProfileUri: Uri
    private lateinit var currentProfileName: String

    private val _profile: MutableLiveData<Uri> = MutableLiveData()
    val profile: LiveData<Uri>
        get() = _profile

    val userNickname: MutableLiveData<String> = MutableLiveData()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    // 처음 프로필 설정 정보를 불러오는 과정. 이것은 전체를 온보딩 작업 내에서 딱 한 번만 일어난다.
    fun setupProfile(defaultUri: Uri) {
        if (_isLoading.value == true) return
        _isLoading.value = true

        viewModelScope.launch {
            when (val response = userRepository.getProfile()) {
                is ApiResponse.Success -> {
                    val profileModel = response.body.toPresentation()
                    val originProfileUri =
                        profileModel.profileImage?.let { Uri.parse(it) } ?: defaultUri
                    _profile.value = originProfileUri
                    userNickname.value = profileModel.name.trim()
                    currentProfileUri = originProfileUri
                    currentProfileName = profileModel.name.trim()
                }

                is ApiResponse.Failure -> {
                    _event.value =
                        Event.FailureInitSetupProfileEvent(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = Event.FailureInitSetupProfileEvent(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = Event.FailureInitSetupProfileEvent(ErrorType.UNEXPECTED)
                }
            }
            _isLoading.value = false
        }
    }

    fun setProfileImageUri(uri: Uri) {
        _profile.value = uri
    }

    fun selectProfileImage() {
        _event.value = Event.NavigateToSelectProfileImage
    }

    fun submitProfile(context: Context) {
        val name = userNickname.value?.trim() ?: return
        val profileImageUri = profile.value?.takeIf { it.path != currentProfileUri.path }

        // 만약 이전과 상태가 같다면, 변경 요청 보내지 않을 것임. 그냥 다음 페이지로 이동
        if (name == currentProfileName && profileImageUri == null) return setNavigateToNextEvent()

        if (_isLoading.value == true) return
        _isLoading.value = true
        viewModelScope.launch {
            val file = runCatching { profileImageUri?.toAdjustImageFile(context) }.getOrNull()
            when (val response = userRepository.updateProfile(file, ProfileUpdateRequest(name))) {
                is ApiResponse.Success -> {
                    currentProfileUri = profileImageUri ?: currentProfileUri
                    setNavigateToNextEvent()
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
            _isLoading.value = false
        }
    }

    private fun setNavigateToNextEvent() {
        _event.value = Event.NavigateToNext
    }

    sealed class Event {
        data class FailureInitSetupProfileEvent(val errorType: ErrorType) : Event()
        data class FailureChangeProfileEvent(val errorType: ErrorType) : Event()
        object NavigateToSelectProfileImage : Event()
        object NavigateToNext : Event()
    }
}
