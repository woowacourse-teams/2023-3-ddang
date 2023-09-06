package com.ddangddangddang.android.feature.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentSearchBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.feature.home.AuctionAdapter
import com.ddangddangddang.android.feature.home.AuctionSpaceItemDecoration
import com.ddangddangddang.android.model.AuctionHomeModel
import com.ddangddangddang.android.model.AuctionHomeStatusModel
import com.ddangddangddang.android.util.binding.BindingFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }
    private val auctionAdapter = AuctionAdapter { auctionId ->
        navigateToAuctionDetail(auctionId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAuctionRecyclerView()
    }

    private fun setupAuctionRecyclerView() {
        binding.rvSearchAuctions.adapter = auctionAdapter.apply {
            submitList(
                listOf(
                    AuctionHomeModel(0, "ihi", "", 1000, AuctionHomeStatusModel.UNBIDDEN, 0),
                    AuctionHomeModel(1, "ihi", "", 1000, AuctionHomeStatusModel.UNBIDDEN, 0),
                    AuctionHomeModel(2, "ihi", "", 1000, AuctionHomeStatusModel.SUCCESS, 0),
                ),
            )
        }
        binding.rvSearchAuctions.addItemDecoration(AuctionSpaceItemDecoration(2, 20))
    }

    private fun navigateToAuctionDetail(auctionId: Long) {
        val intent = AuctionDetailActivity.getIntent(requireContext(), auctionId)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}
