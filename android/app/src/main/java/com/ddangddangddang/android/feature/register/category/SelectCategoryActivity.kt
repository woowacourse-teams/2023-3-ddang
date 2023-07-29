package com.ddangddangddang.android.feature.register.category

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySelectCategoryBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class SelectCategoryActivity :
    BindingActivity<ActivitySelectCategoryBinding>(R.layout.activity_select_category) {
    private val viewModel by viewModels<SelectCategoryViewModel> { viewModelFactory }
    private val mainAdapter by lazy {
        MainCategoryAdapter { id ->
            viewModel.setMainCategorySelection(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.rvMainCategory.addItemDecoration(dividerItemDecoration)

        // Adapter setting
        // Main
        binding.rvMainCategory.adapter = mainAdapter
        mainAdapter.setCategories(viewModel.mainCategories)

        // Sub
        binding.rvSubCategory.adapter =
            SubCategoryAdapter(listOf("컴퓨터", "컴퓨터", "컴퓨터", "컴퓨터", "컴퓨터", "컴퓨터"))

        // Observe
        viewModel.event.observe(this) {
            handleEvent(it)
            Log.d("test", it.toString())
        }
    }

    private fun handleEvent(event: SelectCategoryViewModel.SelectCategoryEvent) {
        when (event) {
            is SelectCategoryViewModel.SelectCategoryEvent.Exit -> finish()
            is SelectCategoryViewModel.SelectCategoryEvent.MainCategoriesChanged -> mainAdapter.setCategories(viewModel.mainCategories)
        }
    }
}
