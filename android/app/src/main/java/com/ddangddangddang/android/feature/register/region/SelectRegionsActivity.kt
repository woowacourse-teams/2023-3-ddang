package com.ddangddangddang.android.feature.register.region

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivitySelectRegionsBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.register.RegisterAuctionActivity
import com.ddangddangddang.android.model.RegionSelectionModel
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.compat.getSerializableExtraCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectRegionsActivity :
    BindingActivity<ActivitySelectRegionsBinding>(R.layout.activity_select_regions) {
    private val viewModel: SelectRegionsViewModel by viewModels()
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
    private val regionSelectionAdapter by lazy {
        RegionSelectionAdapter {
            viewModel.deleteRegion(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupAdapter()
        setupObserve()
        viewModel.loadFirstRegions()
        setupRegionSelected()
    }

    private fun setupAdapter() {
        binding.rvRegionsFirst.adapter = firstRegionsAdapter
        binding.rvRegionsSecond.adapter = secondRegionsAdapter
        binding.rvRegionsThird.adapter = thirdRegionsAdapter
        binding.rvRegionChips.adapter = regionSelectionAdapter
        binding.rvRegionChips.addItemDecoration(RegionSelectionDecoration())
    }

    private fun setupObserve() {
        viewModel.event.observe(this) {
            handleEvent(it)
        }
        viewModel.regionSelections.observe(this) {
            regionSelectionAdapter.setRegions(it)
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
            is SelectRegionsViewModel.SelectRegionsEvent.Submit -> {
                submit(event.regions)
            }

            is SelectRegionsViewModel.SelectRegionsEvent.FirstRegionsLoadFailure -> {
                notifyFailureMessage(event.error, R.string.select_regions_first_failure)
            }

            is SelectRegionsViewModel.SelectRegionsEvent.SecondRegionsLoadFailure -> {
                notifyFailureMessage(event.error, R.string.select_regions_second_failure)
            }

            is SelectRegionsViewModel.SelectRegionsEvent.ThirdRegionsLoadFailure -> {
                notifyFailureMessage(event.error, R.string.select_regions_third_failure)
            }
        }
    }

    private fun submit(regions: List<RegionSelectionModel>) {
        intent.putExtra(RegisterAuctionActivity.REGIONS_RESULT, regions.toTypedArray())
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setupRegionSelected() {
        val regionSelected =
            intent.getSerializableExtraCompat<Array<RegionSelectionModel>>(KEY_REGION_SELECTED)
        regionSelected?.let { viewModel.setupRegionSelected(it.toList()) }
    }

    companion object {
        private const val KEY_REGION_SELECTED = "region_selected"

        fun getIntent(context: Context, regionSelected: List<RegionSelectionModel>): Intent {
            val intent = Intent(context, SelectRegionsActivity::class.java)
            intent.putExtra(KEY_REGION_SELECTED, regionSelected.toTypedArray())
            return intent
        }
    }
}
