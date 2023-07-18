package com.ddangddangddang.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val main: String,
    val sub: String,
)
