package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.response.RegionDetailResponse

interface RegionRepository {
    fun getFirstRegions(): List<RegionDetailResponse>
    fun getSecondRegions(firstId: Long): List<RegionDetailResponse>
    fun getThirdRegions(secondId: Long): List<RegionDetailResponse>
}
