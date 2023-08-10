package com.ddangddangddang.android.feature.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.ProfileModel

class MyPageViewModel : ViewModel() {
    private val _profile: MutableLiveData<ProfileModel> = MutableLiveData(
        ProfileModel(
            "글로",
            "https://img.freepik.com/free-photo/cute-ai-generated-cartoon-bunny_23-2150288870.jpg?q=10&h=200",
            9.8,
        ),
    )
    val profile: LiveData<ProfileModel>
        get() = _profile
}
