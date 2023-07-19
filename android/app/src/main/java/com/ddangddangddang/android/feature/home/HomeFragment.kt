package com.ddangddangddang.android.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentHomeBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingFragment

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAuctionRecyclerView()
    }

    private fun setupAuctionRecyclerView() {
        with(binding.rvAuction) {
            adapter = AuctionAdapter().apply { setAuctions(viewModel.auctions) }
            addItemDecoration(AuctionSpaceItemDecoration(spanCount = 2, space = 20))
        }
    }
}
