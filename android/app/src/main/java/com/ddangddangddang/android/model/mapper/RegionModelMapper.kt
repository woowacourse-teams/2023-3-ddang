package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.RegionSelectionModel
import com.ddangddangddang.data.model.response.RegionDetailResponse

object RegionModelMapper : Mapper<RegionSelectionModel, RegionDetailResponse> {
    override fun RegionDetailResponse.toPresentation(): RegionSelectionModel {
        return RegionSelectionModel(id, name)
    }
}
