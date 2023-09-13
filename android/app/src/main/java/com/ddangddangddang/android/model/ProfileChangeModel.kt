package com.ddangddangddang.android.model

import android.net.Uri

data class ProfileChangeModel(
    val name: String,
    val profileImage: Uri?,
    val reliability: Double,
)
