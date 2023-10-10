package com.ddangddangddang.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceTokenRequest(val deviceToken: String)
