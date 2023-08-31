package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.data.model.response.EachCategoryResponse

object CategoryModelMapper : Mapper<CategoryModel, EachCategoryResponse> {
    override fun EachCategoryResponse.toPresentation(): CategoryModel {
        return CategoryModel(name, id, false)
    }
}
