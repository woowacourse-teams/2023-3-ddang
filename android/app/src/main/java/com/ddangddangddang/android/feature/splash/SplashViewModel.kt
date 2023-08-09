package com.ddangddangddang.android.feature.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<SplashEvent> = SingleLiveEvent()
    val event: LiveData<SplashEvent>
        get() = _event

    fun checkTokenExist() {
        if (repository.getAccessToken().isEmpty()) {
            _event.value = SplashEvent.TokenNotExist
            return
        }
        verifyToken()
    }

    private fun verifyToken() {
        viewModelScope.launch {
            // 액세스 토큰 유효성 검증 받아서, 만약 401 에러 나오면, RefreshTokenExpired
            // 성공하면, AutoLoginSuccess
        }
    }

    sealed class SplashEvent {
        object TokenNotExist : SplashEvent()
        object RefreshTokenExpired : SplashEvent()
        object AutoLoginSuccess : SplashEvent()
    }
}
