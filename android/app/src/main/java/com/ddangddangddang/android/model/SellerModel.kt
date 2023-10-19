package com.ddangddangddang.android.model

data class SellerModel(
    val id: Long,
    val profileUrl: String,
    val nickname: String,
    val reliability: Float?,
) {
    fun toProfileModel(): ProfileModel {
        return ProfileModel(nickname, profileUrl, reliability)
    }
}
