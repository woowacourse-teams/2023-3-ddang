package com.ddangddangddang.android.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentHomeBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.util.binding.BindingFragment

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAuctionRecyclerView()
    }

    private fun setupViewModel() {
        viewModel.event.observe(viewLifecycleOwner) { handleEvent(it) }
    }

    private fun handleEvent(event: HomeViewModel.HomeEvent) {
        when (event) {
            is HomeViewModel.HomeEvent.NavigateToAuctionDetail -> {
                navigateToAuctionDetail(event.auctionId)
            }
        }
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvAuction) {
            adapter = AuctionAdapter { auctionId ->
                viewModel.navigateToAuctionDetail(auctionId)
            }.apply { setAuctions(viewModel.auctions) }
            addItemDecoration(AuctionSpaceItemDecoration(spanCount = 2, space = 20))
        }
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(requireContext(), auctionId)
        startActivity(intent)
    }
}
