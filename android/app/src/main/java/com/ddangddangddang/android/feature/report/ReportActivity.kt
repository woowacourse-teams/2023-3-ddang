package com.ddangddangddang.android.feature.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityReportBinding
import com.ddangddangddang.android.util.binding.BindingActivity

class ReportActivity : BindingActivity<ActivityReportBinding>(R.layout.activity_report) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        private const val AUCTION_ID_KEY = "auction_id_key"
        fun getIntent(context: Context, auctionId: Long): Intent =
            Intent(context, ReportActivity::class.java).apply {
                putExtra(AUCTION_ID_KEY, auctionId)
            }
    }
}
