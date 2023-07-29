package com.ddangddangddang.android.model

data class CategoriesModel(val categories: MutableMap<CategoryModel, List<CategoryModel>?>) {
    fun setSubCategories(mainCategoryId: Long, subCategories: List<CategoryModel>) {
        val main = categories.keys.find { it.id == mainCategoryId }
        if (main != null) categories[main] = subCategories
    }

    data class CategoryModel(val name: String, val id: Long, val isChecked: Boolean = false)
}
