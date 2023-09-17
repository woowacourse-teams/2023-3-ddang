package com.ddangddangddang.android.feature.participateAuction

import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityParticipateAuctionBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class ParticipateAuctionActivity :
    BindingActivity<ActivityParticipateAuctionBinding>(R.layout.activity_participate_auction) {
    private val viewModel: ParticipateAuctionViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
    }
}
