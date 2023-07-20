package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DirectRegionRequest(val first: String, val second: String, val third: String)
