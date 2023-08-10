package com.ddangddangddang.android.feature.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityReportBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class ReportActivity : BindingActivity<ActivityReportBinding>(R.layout.activity_report) {
    private val viewModel: ReportViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.event.observe(this) { event ->
            when (event) {
                ReportViewModel.ReportEvent.ExitEvent -> finish()
            }
        }
    }

    companion object {
        private const val AUCTION_ID_KEY = "auction_id_key"
        fun getIntent(context: Context, auctionId: Long): Intent =
            Intent(context, ReportActivity::class.java).apply {
                putExtra(AUCTION_ID_KEY, auctionId)
            }
    }
}
