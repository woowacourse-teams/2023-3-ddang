package com.ddangddangddang.android.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.KakaoLoginRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel(
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
            val request = KakaoLoginRequest(accessToken, deviceToken)
            when (val response = repository.loginByKakao(request)) {
                is ApiResponse.Success -> _event.value = LoginEvent.CompleteLoginEvent
                is ApiResponse.Failure -> _event.value = LoginEvent.FailureLoginEvent(response.error)
                is ApiResponse.NetworkError -> _event.value = LoginEvent.FailureLoginEvent(response.exception.message)
                is ApiResponse.Unexpected -> _event.value = LoginEvent.FailureLoginEvent(response.t?.message)
            }
        }
    }

    sealed class LoginEvent {
        object KakaoLoginEvent : LoginEvent()
        object CompleteLoginEvent : LoginEvent()
        data class FailureLoginEvent(val message: String?) : LoginEvent()
    }
}
