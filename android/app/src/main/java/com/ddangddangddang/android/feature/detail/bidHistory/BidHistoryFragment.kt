package com.ddangddangddang.android.feature.detail.bidHistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentBidHistoryBinding
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BidHistoryFragment :
    BindingFragment<FragmentBidHistoryBinding>(R.layout.fragment_bid_history) {
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()
    private val viewModel: BidHistoryViewModel by viewModels()
    private val adapter by lazy { BidHistoryAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBidHistory.adapter = adapter
        setupViewModel()
    }

    private fun setupViewModel() {
        activityViewModel.auctionDetailModel.observe(viewLifecycleOwner) {
            viewModel.loadBidHistory()
        }
    }
}
