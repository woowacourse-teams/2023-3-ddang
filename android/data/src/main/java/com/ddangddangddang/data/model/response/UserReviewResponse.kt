package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserReviewResponse(val score: Double?, val content: String?)
