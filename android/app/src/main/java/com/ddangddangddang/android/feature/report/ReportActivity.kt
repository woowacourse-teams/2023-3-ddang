package com.ddangddangddang.android.feature.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityReportBinding
import com.ddangddangddang.android.model.ReportType
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.compat.getParcelableCompat
import com.ddangddangddang.android.util.view.Toaster
import com.ddangddangddang.android.util.view.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportActivity : BindingActivity<ActivityReportBinding>(R.layout.activity_report) {
    private val viewModel: ReportViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        getReportInfo()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.event.observe(this) { event ->
            when (event) {
                ReportViewModel.ReportEvent.ExitEvent -> finish()
                ReportViewModel.ReportEvent.SubmitEvent -> submit()
                ReportViewModel.ReportEvent.BlankContentsEvent -> notifyBlankContents()
            }
        }
    }

    private fun getReportInfo() {
        val type = intent.getParcelableCompat<ReportType>(REPORT_TYPE_KEY)
            ?: return notifyReportTypeNotDelivered()
        val id = intent.getLongExtra(REPORT_ID_KEY, DEFAULT_VALUE)
        if (id == DEFAULT_VALUE) notifyReportIdNotDelivered()
        viewModel.setReportInfo(type, id)
    }

    private fun submit() {
        Toaster.showShort(this, getString(R.string.report_snackbar_complete))
        finish()
    }

    private fun notifyBlankContents() {
        binding.root.showSnackbar(textId = R.string.report_snackbar_blank_contents)
    }

    private fun notifyReportTypeNotDelivered() {
        Toaster.showShort(this, getString(R.string.report_snackbar_auction_id_not_delivered))
        finish()
    }

    private fun notifyReportIdNotDelivered() {
        Toaster.showShort(this, getString(R.string.report_snackbar_auction_id_not_delivered))
        finish()
    }

    companion object {
        private const val DEFAULT_VALUE = -1L
        private const val REPORT_TYPE_KEY = "report_type_key"
        private const val REPORT_ID_KEY = "report_id_key"
        fun getIntent(context: Context, reportType: ReportType, reportId: Long): Intent =
            Intent(context, ReportActivity::class.java).apply {
                putExtra(REPORT_TYPE_KEY, reportType as Parcelable)
                putExtra(REPORT_ID_KEY, reportId)
            }
    }
}
