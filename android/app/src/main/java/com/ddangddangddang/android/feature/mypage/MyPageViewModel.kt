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
    private val _profile: MutableLiveData<ProfileModel> = MutableLiveData(
        ProfileModel(
            "글로",
            "https://img.freepik.com/free-photo/cute-ai-generated-cartoon-bunny_23-2150288870.jpg?q=10&h=200",
            9.8,
        ),
    )
    val profile: LiveData<ProfileModel>
        get() = _profile

    private val _event: SingleLiveEvent<MyPageEvent> = SingleLiveEvent()
    val event: LiveData<MyPageEvent>
        get() = _event

    fun loadProfile() {
        viewModelScope.launch {
            when (val response = userRepository.getProfile()) {
                is ApiResponse.Success -> {
                    _profile.value = response.body.toPresentation()
                }

                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
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
        object LogoutSuccessfully : MyPageEvent()
        object LogoutFailed : MyPageEvent()
    }
}
