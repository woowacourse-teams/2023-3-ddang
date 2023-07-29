package com.ddangddangddang.android.feature.register.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.CategoriesModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class SelectCategoryViewModel : ViewModel() {
    private val _event: SingleLiveEvent<SelectCategoryEvent> = SingleLiveEvent()
    val event: LiveData<SelectCategoryEvent>
        get() = _event

    private val _mainCategories = mutableListOf(
        CategoriesModel.CategoryModel("가전 제품1", 0, true),
        CategoriesModel.CategoryModel("가전 제품2", 1, false),
        CategoriesModel.CategoryModel("가전 제품3", 2, false),
        CategoriesModel.CategoryModel("가전 제품4", 3, false),
        CategoriesModel.CategoryModel("가전 제품5", 4, false),
    )
    val mainCategories: List<CategoriesModel.CategoryModel>
        get() = _mainCategories.toList() // DiffUtil은 리스트의 주소가 다르지 않으면 Difference를 검사 안함

    private val _subCategories = mutableListOf(
        CategoriesModel.CategoryModel("노트북", 0, true),
        CategoriesModel.CategoryModel("헤드셋", 1, false),
        CategoriesModel.CategoryModel("태블릿PC", 2, false),
    )
    val subCategories: List<CategoriesModel.CategoryModel>
        get() = _subCategories.toList() // DiffUtil은 리스트의 주소가 다르지 않으면 Difference를 검사 안함

    fun setExitEvent() {
        _event.value = SelectCategoryEvent.Exit
    }

    fun setMainCategorySelection(mainCategoryId: Long) {
        _mainCategories.replaceAll {
            if (it.id == mainCategoryId) {
                it.copy(isChecked = true)
            } else if (it.isChecked) {
                it.copy(isChecked = false)
            } else {
                it
            }
        }
        _event.value = SelectCategoryEvent.MainCategoriesChanged
    }

    fun setSubCategorySelection(subCategoryId: Long) {
        _subCategories.replaceAll {
            if (it.id == subCategoryId) {
                it.copy(isChecked = true)
            } else if (it.isChecked) {
                it.copy(isChecked = false)
            } else {
                it
            }
        }
        _event.value = SelectCategoryEvent.SubCategoriesChanged
    }

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
        object MainCategoriesChanged : SelectCategoryEvent()
        object SubCategoriesChanged : SelectCategoryEvent()
    }
}
