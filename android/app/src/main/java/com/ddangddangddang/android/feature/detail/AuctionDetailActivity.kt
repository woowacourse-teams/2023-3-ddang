package com.ddangddangddang.android.feature.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityAuctionDetailBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class AuctionDetailActivity :
    BindingActivity<ActivityAuctionDetailBinding>(R.layout.activity_auction_detail) {
    private val viewModel: AuctionDetailViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        val auctionId = intent.getLongExtra(AUCTION_ID_KEY, -1L)
        setupViewModel(auctionId)
    }

    private fun setupViewModel(auctionId: Long) {
        // 경매 번호를 뷰모델에 넘겨서 불러온다
        // 옵저버로 관찰한다
        viewModel.event.observe(this) {
            finish()
        }
    }

    companion object {
        private const val AUCTION_ID_KEY = "auction_id_key"

        fun getIntent(context: Context, auctionId: Long): Intent {
            return Intent(context, AuctionDetailActivity::class.java).apply {
                putExtra(AUCTION_ID_KEY, auctionId)
            }
        }
    }
}
