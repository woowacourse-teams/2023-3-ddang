package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.response.RegionDetailResponse

class RegionRepositoryMockImpl : RegionRepository {
    private val firstRegions = listOf(
        RegionDetailResponse(0, "서울특별시"),
        RegionDetailResponse(1, "경기도"),
        RegionDetailResponse(2, "인천광역시"),
    )
    private val secondRegions: Map<Int, List<RegionDetailResponse>> = mapOf(
        0 to listOf(
            RegionDetailResponse(3, "동작구"),
            RegionDetailResponse(4, "강남구"),
            RegionDetailResponse(5, "양천구"),
        ),
        1 to listOf(
            RegionDetailResponse(6, "시흥시"),
            RegionDetailResponse(7, "부천시"),
            RegionDetailResponse(8, "광명시"),
        ),
        2 to listOf(
            RegionDetailResponse(9, "서구"),
            RegionDetailResponse(10, "동구"),
            RegionDetailResponse(11, "연수구"),
        ),
    )

    private val thirdRegions: Map<Int, List<RegionDetailResponse>> = mapOf(
        3 to listOf(
            RegionDetailResponse(12, "노량진1동"),
            RegionDetailResponse(13, "상도1동"),
            RegionDetailResponse(14, "흑석동"),
        ),
        4 to listOf(
            RegionDetailResponse(15, "시흥시시"),
            RegionDetailResponse(16, "부천시시"),
            RegionDetailResponse(17, "광명시시"),
        ),
        5 to listOf(
            RegionDetailResponse(18, "서구구"),
            RegionDetailResponse(19, "동구구"),
            RegionDetailResponse(20, "연수구구"),
        ),
        6 to listOf(
            RegionDetailResponse(21, "동작구구"),
            RegionDetailResponse(22, "강남구구"),
            RegionDetailResponse(23, "양천구구"),
        ),
        7 to listOf(
            RegionDetailResponse(24, "시흥시시"),
            RegionDetailResponse(25, "부천시시"),
            RegionDetailResponse(26, "광명시시"),
        ),
        8 to listOf(
            RegionDetailResponse(27, "서구구"),
            RegionDetailResponse(28, "동구구"),
            RegionDetailResponse(29, "연수구구"),
        ),
        9 to listOf(
            RegionDetailResponse(30, "동작구구"),
            RegionDetailResponse(31, "강남구구"),
            RegionDetailResponse(32, "양천구구"),
        ),
        10 to listOf(
            RegionDetailResponse(33, "시흥시시"),
            RegionDetailResponse(34, "부천시시"),
            RegionDetailResponse(35, "광명시시"),
        ),
        11 to listOf(
            RegionDetailResponse(36, "서구구"),
            RegionDetailResponse(37, "동구구"),
            RegionDetailResponse(38, "연수구구"),
        ),
    )

    override fun getFirstRegions(): List<RegionDetailResponse> {
        return firstRegions
    }

    override fun getSecondRegions(firstId: Long): List<RegionDetailResponse> {
        return secondRegions[firstId.toInt()] ?: emptyList()
    }

    override fun getThirdRegions(secondId: Long): List<RegionDetailResponse> {
        return thirdRegions[secondId.toInt()] ?: emptyList()
    }
}
