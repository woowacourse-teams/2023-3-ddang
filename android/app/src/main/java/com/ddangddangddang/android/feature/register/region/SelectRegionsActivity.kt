package com.ddangddangddang.android.feature.register.region

import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySelectRegionsBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity
import com.google.android.material.chip.Chip

class SelectRegionsActivity :
    BindingActivity<ActivitySelectRegionsBinding>(R.layout.activity_select_regions) {
    private val viewModel by viewModels<SelectRegionsViewModel> { viewModelFactory }
    private val firstRegionsAdapter by lazy {
        FirstRegionsAdapter {
            viewModel.setFirstRegionSelection(it)
        }
    }
    private val secondRegionsAdapter by lazy {
        SecondRegionsAdapter {
            viewModel.setSecondRegionSelection(it)
        }
    }
    private val thirdRegionsAdapter by lazy {
        ThirdRegionsAdapter {
            viewModel.addRegion(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapter()
        setupObserve()
    }

    private fun setupAdapter() {
        binding.rvRegionsFirst.adapter = firstRegionsAdapter
        binding.rvRegionsSecond.adapter = secondRegionsAdapter
        binding.rvRegionsThird.adapter = thirdRegionsAdapter
    }

    private fun setupObserve() {
        viewModel.event.observe(this) {
            handleEvent(it)
        }
        viewModel.firstRegions.observe(this) {
            firstRegionsAdapter.setRegions(it)
        }
        viewModel.secondRegions.observe(this) {
            secondRegionsAdapter.setRegions(it)
        }
        viewModel.thirdRegions.observe(this) {
            thirdRegionsAdapter.setRegions(it)
        }
    }

    private fun handleEvent(event: SelectRegionsViewModel.SelectRegionsEvent) {
        when (event) {
            is SelectRegionsViewModel.SelectRegionsEvent.Exit -> finish()
            is SelectRegionsViewModel.SelectRegionsEvent.AddRegion -> {
                addRegionChip(event.id, event.regionName)
            }
        }
    }

    private fun addRegionChip(id: Long, name: String) {
        binding.cgRegionChips.addView(
            Chip(this).apply {
                text = name
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    viewModel.deleteRegion(id)
                    binding.cgRegionChips.removeView(this)
                }
            },
        )
    }
}
