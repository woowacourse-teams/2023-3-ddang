package com.ddangddangddang.android.model

data class RegionModel(
    val first: String,
    val second: String? = null,
    val third: String? = null,
) {
    fun toRegionName(): String {
        return when {
            second == null -> first
            third == null -> "$first $second"
            else -> "$first $second $third"
        }
    }
}
