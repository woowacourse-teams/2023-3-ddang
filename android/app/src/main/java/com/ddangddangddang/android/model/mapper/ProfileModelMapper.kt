package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.data.model.response.ProfileResponse

object ProfileModelMapper : Mapper<ProfileModel, ProfileResponse> {
    override fun ProfileResponse.toPresentation(): ProfileModel {
        return ProfileModel(name, profileImage, reliability)
    }
}
