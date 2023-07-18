package com.ddangddangddang.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegionResponse(
    val first: String,
    val second: String,
    val third: String,
)
