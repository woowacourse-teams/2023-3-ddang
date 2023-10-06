package com.ddangddangddang.android.feature.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.model.mapper.ProfileModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuthRepository
import com.ddangddangddang.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
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

                is ApiResponse.Failure -> {
                    _event.value = MyPageEvent.LoadProfileFailed(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = MyPageEvent.LoadProfileFailed(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = MyPageEvent.LoadProfileFailed(ErrorType.UNEXPECTED)
                }
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

    fun navigateToNotificationSettings() {
        _event.value = MyPageEvent.NavigateToNotificationSettings
    }

    fun navigateToAnnouncement() {
        _event.value = MyPageEvent.NavigateToAnnouncement
    }

    fun contactUs() {
        _event.value = MyPageEvent.ContactUs
    }

    fun navigateToPrivacyPolicy() {
        _event.value = MyPageEvent.NavigateToPrivacyPolicy
    }

    fun logout() {
        viewModelScope.launch {
            when (val response = authRepository.logout()) {
                is ApiResponse.Success -> {
                    _event.value = MyPageEvent.LogoutSuccessfully
                }

                is ApiResponse.Failure -> {
                    _event.value = MyPageEvent.LogoutFailed(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = MyPageEvent.WithdrawalFailed(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = MyPageEvent.WithdrawalFailed(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    fun askWithdrawal() {
        _event.value = MyPageEvent.AskWithdrawal
    }

    fun withdrawal() {
        viewModelScope.launch {
            when (val response = authRepository.withdrawal()) {
                is ApiResponse.Success -> {
                    _event.value = MyPageEvent.WithdrawalSuccessfully
                }

                is ApiResponse.Failure -> {
                    _event.value = MyPageEvent.WithdrawalFailed(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = MyPageEvent.WithdrawalFailed(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = MyPageEvent.WithdrawalFailed(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    sealed class MyPageEvent {
        data class LoadProfileFailed(val type: ErrorType) : MyPageEvent()
        object ProfileChange : MyPageEvent()
        object NavigateToMyAuctions : MyPageEvent()
        object NavigateToMyParticipateAuctions : MyPageEvent()
        object NavigateToNotificationSettings : MyPageEvent()
        object NavigateToAnnouncement : MyPageEvent()
        object ContactUs : MyPageEvent()
        object NavigateToPrivacyPolicy : MyPageEvent()
        object LogoutSuccessfully : MyPageEvent()
        data class LogoutFailed(val type: ErrorType) : MyPageEvent()
        object AskWithdrawal : MyPageEvent()
        object WithdrawalSuccessfully : MyPageEvent()
        data class WithdrawalFailed(val type: ErrorType) : MyPageEvent()
    }
}
