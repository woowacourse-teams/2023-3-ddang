package com.ddangddangddang.android.feature.detail.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentAuctionInfoBinding
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.model.RegionModel
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuctionInfoFragment :
    BindingFragment<FragmentAuctionInfoBinding>(R.layout.fragment_auction_info) {
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.activityViewModel = activityViewModel
        setupViewModel()
    }

    private fun setupViewModel() {
        activityViewModel.auctionDetailModel.observe(viewLifecycleOwner) {
            setupDirectRegions(it.directRegions)
        }
    }

    private fun setupDirectRegions(regions: List<RegionModel>) {
        binding.rvDirectExchangeRegions.adapter = AuctionDirectRegionsAdapter(regions)
    }
}
