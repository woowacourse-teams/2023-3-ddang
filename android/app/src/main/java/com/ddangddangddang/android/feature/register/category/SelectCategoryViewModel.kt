package com.ddangddangddang.android.feature.register.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.CategoriesModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent

class SelectCategoryViewModel : ViewModel() {
    private val _event: SingleLiveEvent<SelectCategoryEvent> = SingleLiveEvent()
    val event: LiveData<SelectCategoryEvent>
        get() = _event

    private var mainCategorySelection: Long = 0 // id
    private var subCategorySelection: Long? = null

    private val _mainCategories = MutableLiveData<List<CategoriesModel.CategoryModel>>(
        listOf(
            CategoriesModel.CategoryModel("가전 제품1", 0, true),
            CategoriesModel.CategoryModel("가전 제품2", 1, false),
            CategoriesModel.CategoryModel("가전 제품3", 2, false),
            CategoriesModel.CategoryModel("가전 제품4", 3, false),
            CategoriesModel.CategoryModel("가전 제품5", 4, false),
        ),
    )
    val mainCategories: LiveData<List<CategoriesModel.CategoryModel>>
        get() = _mainCategories

    fun setExitEvent() {
        _event.value = SelectCategoryEvent.Exit
    }

    fun setMainCategorySelection(mainCategoryId: Long) {
        val temp = _mainCategories.value?.toMutableList()
        temp?.let {
            val index = temp.indexOfFirst { it.id == mainCategoryId }
            temp[index] = temp[index].copy(isChecked = true)
            _mainCategories.value = temp.toList()
        }
        Log.d("test", mainCategories.value.toString())
    }

    sealed class SelectCategoryEvent {
        object Exit : SelectCategoryEvent()
    }
}
