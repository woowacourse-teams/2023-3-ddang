package com.ddangddangddang.android.feature.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.MarginPageTransformer
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityAuctionDetailBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity
import com.google.android.material.tabs.TabLayoutMediator

class AuctionDetailActivity :
    BindingActivity<ActivityAuctionDetailBinding>(R.layout.activity_auction_detail) {
    private val viewModel: AuctionDetailViewModel by viewModels { viewModelFactory }

    private val images = listOf(
        "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
        "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
        "https://cdn.pixabay.com/photo/2020/03/08/21/41/landscape-4913841_1280.jpg",
        "https://cdn.pixabay.com/photo/2020/09/02/18/03/girl-5539094_1280.jpg",
        "https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        val auctionId = intent.getLongExtra(AUCTION_ID_KEY, -1L)
        setupViewModel(auctionId)

        binding.vpImageList.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1
            adapter = AuctionImageAdapter(images)
            setPageTransformer(MarginPageTransformer(100))
            setPadding(200, 0, 200, 0)
        }

        TabLayoutMediator(binding.tlIndicator, binding.vpImageList) { tab, position -> }.attach()
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
