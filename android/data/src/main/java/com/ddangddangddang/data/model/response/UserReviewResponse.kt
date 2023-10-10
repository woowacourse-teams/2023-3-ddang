package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserReviewResponse(val score: Float?, val content: String?)
