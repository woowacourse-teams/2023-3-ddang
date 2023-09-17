package com.ddangddangddang.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileModel(
    val name: String,
    val profileImage: String?,
    val reliability: Double,
) : Parcelable
