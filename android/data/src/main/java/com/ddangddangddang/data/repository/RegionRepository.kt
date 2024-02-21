package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.response.RegionDetailResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse

interface RegionRepository {
    suspend fun getFirstRegions(): ApiResponse<List<RegionDetailResponse>>
    suspend fun getSecondRegions(firstId: Long): ApiResponse<List<RegionDetailResponse>>
    suspend fun getThirdRegions(firstId: Long, secondId: Long): ApiResponse<List<RegionDetailResponse>>
}
