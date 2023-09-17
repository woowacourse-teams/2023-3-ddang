package com.ddangddangddang.android.feature.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.model.mapper.ProfileModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuthRepository
import com.ddangddangddang.data.repository.UserRepository
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _profile: MutableLiveData<ProfileModel> = MutableLiveData()
    val profile: LiveData<ProfileModel>
        get() = _profile

    private val _event: SingleLiveEvent<MyPageEvent> = SingleLiveEvent()
    val event: LiveData<MyPageEvent>
        get() = _event

    fun loadProfile() {
        if (_isLoading.value == true) return
        _isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getProfile()) {
                is ApiResponse.Success -> {
                    _profile.value = response.body.toPresentation()
                }

                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
            _isLoading.value = false
        }
    }

    fun changeProfile() {
        _event.value = MyPageEvent.ProfileChange
    }

    fun navigateToMyAuctions() {
        _event.value = MyPageEvent.NavigateToMyAuctions
    }

    fun navigateToMyParticipateAuctions() {
        _event.value = MyPageEvent.NavigateToMyParticipateAuctions
    }

    fun navigateToAnnouncement() {
        _event.value = MyPageEvent.NavigateToAnnouncement
    }

    fun navigateToPrivacyPolicy() {
        _event.value = MyPageEvent.NavigateToPrivacyPolicy
    }

    fun logout() {
        viewModelScope.launch {
            when (authRepository.logout()) {
                is ApiResponse.Success -> {
                    _event.value = MyPageEvent.LogoutSuccessfully
                }

                is ApiResponse.Failure -> {
                    _event.value = MyPageEvent.LogoutFailed
                }

                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    sealed class MyPageEvent {
        object ProfileChange : MyPageEvent()
        object NavigateToMyAuctions : MyPageEvent()
        object NavigateToMyParticipateAuctions : MyPageEvent()
        object NavigateToAnnouncement : MyPageEvent()
        object NavigateToPrivacyPolicy : MyPageEvent()
        object LogoutSuccessfully : MyPageEvent()
        object LogoutFailed : MyPageEvent()
    }
}
