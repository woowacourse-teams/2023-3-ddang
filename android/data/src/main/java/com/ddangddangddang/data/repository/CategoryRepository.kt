package com.ddangddangddang.data.repository

import com.ddangddangddang.data.model.response.CategoriesResponse

interface CategoryRepository {
    fun getMainCategories(): CategoriesResponse

    fun getSubCategories(mainCategoryId: Long): CategoriesResponse
}
