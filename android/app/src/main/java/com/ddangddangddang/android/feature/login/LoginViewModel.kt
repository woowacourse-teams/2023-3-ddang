package com.ddangddangddang.android.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
) : ViewModel() {
    private val _event: SingleLiveEvent<LoginEvent> = SingleLiveEvent()
    val event: LiveData<LoginEvent>
        get() = _event

    fun loginByKakao() {
        _event.value = LoginEvent.KakaoLoginEvent
    }

    fun completeLoginByKakao(accessToken: String) {
        viewModelScope.launch {
            val deviceToken = repository.getDeviceToken()
            if (deviceToken.isNullOrBlank()) {
                _event.value = LoginEvent.FailureLoginEvent(ErrorType.UNEXPECTED)
                return@launch
            }

            val request = KakaoLoginRequest(accessToken, deviceToken)
            when (val response = repository.loginByKakao(request)) {
                is ApiResponse.Success -> {
                    if (response.body.isSignUpUser) {
                        _event.value = LoginEvent.SignUpEvent
                    } else {
                        _event.value = LoginEvent.CompleteLoginEvent
                    }
                }

                is ApiResponse.Failure -> {
                    _event.value = LoginEvent.FailureLoginEvent(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = LoginEvent.FailureLoginEvent(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = LoginEvent.FailureLoginEvent(ErrorType.UNEXPECTED)
                }
            }
        }
    }

    sealed class LoginEvent {
        object KakaoLoginEvent : LoginEvent()
        object SignUpEvent : LoginEvent()
        object CompleteLoginEvent : LoginEvent()
        data class FailureLoginEvent(val type: ErrorType) : LoginEvent()
    }
}
