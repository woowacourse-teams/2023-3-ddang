package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(val main: String, val sub: String)
