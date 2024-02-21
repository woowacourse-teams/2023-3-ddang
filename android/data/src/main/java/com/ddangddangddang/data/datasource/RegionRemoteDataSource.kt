package com.ddangddangddang.data.datasource

import com.ddangddangddang.data.model.response.RegionDetailResponse
import com.ddangddangddang.data.remote.callAdapter.ApiResponse
import com.ddangddangddang.data.remote.retrofit.AuctionService
import javax.inject.Inject

class RegionRemoteDataSource @Inject constructor(private val service: AuctionService) {
    suspend fun getFirstRegions(): ApiResponse<List<RegionDetailResponse>> =
        service.fetchFirstRegions()

    suspend fun getSecondRegions(firstId: Long): ApiResponse<List<RegionDetailResponse>> =
        service.fetchSecondRegions(firstId)

    suspend fun getThirdRegions(
        firstId: Long,
        secondId: Long,
    ): ApiResponse<List<RegionDetailResponse>> = service.fetchThirdRegions(firstId, secondId)
}
