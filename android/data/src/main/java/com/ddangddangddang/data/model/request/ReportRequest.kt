package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(val auctionId: Long, val description: String)
