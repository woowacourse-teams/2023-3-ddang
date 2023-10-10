package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatPartnerResponse(
    val id: Long,
    val name: String,
    val profileImage: String? = null,
)
