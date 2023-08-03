package com.ddangddangddang.android.feature.register.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySelectCategoryBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.register.RegisterAuctionActivity
import com.ddangddangddang.android.util.binding.BindingActivity

class SelectCategoryActivity :
    BindingActivity<ActivitySelectCategoryBinding>(R.layout.activity_select_category) {
    private val viewModel by viewModels<SelectCategoryViewModel> { viewModelFactory }
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
                val intent = Intent().apply {
                    intent.putExtra(RegisterAuctionActivity.CATEGORY_RESULT, event.category)
                }
                setResult(RESULT_OK, intent)
                finish()
                Log.d("test", "Submit ${event.category}") // 코드 작성 전까지 확인용
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, SelectCategoryActivity::class.java)
    }
}
