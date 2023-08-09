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
            val kakaoToken = KakaoLoginRequest(accessToken)
            when (repository.loginByKakao(kakaoToken)) {
                is ApiResponse.Success -> {}
                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    sealed class LoginEvent {
        object KakaoLoginEvent : LoginEvent()
    }
}
