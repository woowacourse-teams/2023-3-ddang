package com.ddangddangddang.android.feature.register.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySelectCategoryBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.register.RegisterAuctionActivity
import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.android.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryActivity :
    BindingActivity<ActivitySelectCategoryBinding>(R.layout.activity_select_category) {
    private val viewModel: SelectCategoryViewModel by viewModels()
    private val mainAdapter by lazy {
        MainCategoryAdapter { id ->
            viewModel.setMainCategorySelection(id)
        }
    }
    private val subAdapter by lazy {
        SubCategoryAdapter { id ->
            viewModel.submitCategory(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupDecoration()
        setupAdapter()
        setupObserve()
        viewModel.loadMainCategories()
    }

    private fun setupDecoration() {
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.rvMainCategory.addItemDecoration(dividerItemDecoration)
    }

    private fun setupAdapter() {
        // Main
        binding.rvMainCategory.adapter = mainAdapter

        // Sub
        binding.rvSubCategory.adapter = subAdapter
    }

    private fun setupObserve() {
        viewModel.event.observe(this) {
            handleEvent(it)
        }
        viewModel.mainCategories.observe(this) {
            mainAdapter.setCategories(it)
        }
        viewModel.subCategories.observe(this) {
            subAdapter.setCategories(it)
        }
    }

    private fun handleEvent(event: SelectCategoryViewModel.SelectCategoryEvent) {
        when (event) {
            is SelectCategoryViewModel.SelectCategoryEvent.Exit -> {
                finish()
            }

            is SelectCategoryViewModel.SelectCategoryEvent.Submit -> {
                submit(event.category)
            }

            is SelectCategoryViewModel.SelectCategoryEvent.MainCategoriesLoadFailure -> {
                notifyFailureMessage(event.error, R.string.select_category_main_load_failure)
            }

            is SelectCategoryViewModel.SelectCategoryEvent.SubCategoriesLoadFailure -> {
                notifyFailureMessage(event.error, R.string.select_category_sub_load_failure)
            }
        }
    }

    private fun submit(category: CategoryModel) {
        intent.putExtra(RegisterAuctionActivity.CATEGORY_RESULT, category)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, SelectCategoryActivity::class.java)
    }
}
