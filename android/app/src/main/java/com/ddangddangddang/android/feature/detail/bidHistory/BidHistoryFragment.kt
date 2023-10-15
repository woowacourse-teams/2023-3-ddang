package com.ddangddangddang.android.feature.detail.bidHistory

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentBidHistoryBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BidHistoryFragment :
    BindingFragment<FragmentBidHistoryBinding>(R.layout.fragment_bid_history) {
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()
    private val viewModel: BidHistoryViewModel by viewModels()

    @Inject
    lateinit var bidHistoryAdapter: BidHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupAdapter()
        setupViewModel()
    }

    private fun setupAdapter() {
        with(binding.rvBidHistory) {
            adapter = bidHistoryAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
        }
    }

    private fun setupViewModel() {
        activityViewModel.auctionDetailModel.observe(viewLifecycleOwner) {
            viewModel.loadBidHistory(it.id)
        }
        viewModel.histories.observe(viewLifecycleOwner) {
            bidHistoryAdapter.setBidHistories(it)
        }
        viewModel.event.observe(viewLifecycleOwner) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: BidHistoryViewModel.Event) {
        when (event) {
            is BidHistoryViewModel.Event.BidHistoryLoadFailure -> {
                requireActivity().notifyFailureMessage(
                    event.error,
                    R.string.detail_auction_bid_history_load_failure,
                )
            }
        }
    }
}
