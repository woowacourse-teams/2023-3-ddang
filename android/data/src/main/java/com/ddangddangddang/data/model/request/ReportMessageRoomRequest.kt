package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReportMessageRoomRequest(val chatRoomId: Long, val description: String)
