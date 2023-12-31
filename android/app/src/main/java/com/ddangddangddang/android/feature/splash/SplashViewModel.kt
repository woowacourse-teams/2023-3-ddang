package com.ddangddangddang.android.feature.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
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
            when (val response = repository.verifyToken()) {
                is ApiResponse.Success -> {
                    if (response.body.validated) {
                        _event.value = SplashEvent.AutoLoginSuccess
                    } else {
                        refreshToken()
                    }
                }

                is ApiResponse.Failure ->
                    _event.value =
                        SplashEvent.FailureStartDdangDdangDdang(ErrorType.FAILURE(null))

                is ApiResponse.NetworkError ->
                    _event.value =
                        SplashEvent.FailureStartDdangDdangDdang(ErrorType.NETWORK_ERROR)

                is ApiResponse.Unexpected ->
                    _event.value =
                        SplashEvent.FailureStartDdangDdangDdang(ErrorType.UNEXPECTED)
            }
        }
    }

    private suspend fun refreshToken() {
        when (val response = repository.refreshToken()) {
            is ApiResponse.Success -> {
                _event.value = SplashEvent.AutoLoginSuccess
            }

            is ApiResponse.Failure -> {
                if (response.responseCode == 401) {
                    _event.value = SplashEvent.RefreshTokenExpired
                } else {
                    _event.value = SplashEvent.FailureStartDdangDdangDdang(ErrorType.FAILURE(null))
                }
            }

            is ApiResponse.NetworkError ->
                _event.value =
                    SplashEvent.FailureStartDdangDdangDdang(ErrorType.NETWORK_ERROR)

            is ApiResponse.Unexpected ->
                _event.value =
                    SplashEvent.FailureStartDdangDdangDdang(ErrorType.UNEXPECTED)
        }
    }

    sealed class SplashEvent {
        object TokenNotExist : SplashEvent()
        object RefreshTokenExpired : SplashEvent()
        object AutoLoginSuccess : SplashEvent()
        data class FailureStartDdangDdangDdang(val errorType: ErrorType) : SplashEvent()
    }
}
