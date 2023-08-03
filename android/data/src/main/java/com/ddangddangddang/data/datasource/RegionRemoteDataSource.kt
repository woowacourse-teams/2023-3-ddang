package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.response.RegionDetailResponse
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.remote.Service

class RegionRemoteDataSource(private val service: Service) {
    suspend fun getFirstRegions(): ApiResponse<List<RegionDetailResponse>> =
        service.fetchFirstRegions()

    suspend fun getSecondRegions(firstId: Long): ApiResponse<List<RegionDetailResponse>> =
        service.fetchSecondRegions(firstId)

    suspend fun getThirdRegions(
        firstId: Long,
        secondId: Long,
    ): ApiResponse<List<RegionDetailResponse>> = service.fetchThirdRegions(firstId, secondId)
}
