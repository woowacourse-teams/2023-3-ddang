package com.ddangddangddang.android.feature.detail.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentAuctionInfoBinding
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.util.binding.BindingFragment

class AuctionInfoFragment :
    BindingFragment<FragmentAuctionInfoBinding>(R.layout.fragment_auction_info) {
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.activityViewModel = activityViewModel
    }
}
