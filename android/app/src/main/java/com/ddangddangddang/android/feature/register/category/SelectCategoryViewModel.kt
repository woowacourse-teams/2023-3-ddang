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

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
        object MainCategoriesChanged : SelectCategoryEvent()
    }
}
