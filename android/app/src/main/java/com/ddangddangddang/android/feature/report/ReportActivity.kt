package com.ddangddangddang.android.feature.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityReportBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.model.ReportInfo
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.compat.getSerializableExtraCompat
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
                is ReportViewModel.ReportEvent.ReportFailure -> {
                    notifyFailureMessage(event.error, R.string.report_submit_failure)
                }
            }
        }
    }

    private fun getReportInfo() {
        val typeIndex: Int = intent.getIntExtra(REPORT_TYPE_KEY, DEFAULT_VALUE.toInt())
        val info = intent.getSerializableExtraCompat<ReportInfo>(REPORT_ID_KEY)
        if (info == null || typeIndex == DEFAULT_VALUE.toInt()) {
            notifyNavigateToReportPageFailed()
            return
        }
        viewModel.setReportInfo(info)
    }

    private fun submit() {
        Toaster.showShort(this, getString(R.string.report_snackbar_complete))
        finish()
    }

    private fun notifyBlankContents() {
        binding.root.showSnackbar(textId = R.string.report_snackbar_blank_contents)
    }

    private fun notifyNavigateToReportPageFailed() {
        Toaster.showShort(this, getString(R.string.report_snackbar_navigate_to_report_page_failed))
        finish()
    }

    companion object {
        private const val DEFAULT_VALUE = -1L
        private const val REPORT_TYPE_KEY = "report_type_key"
        private const val REPORT_ID_KEY = "report_id_key"
        fun getIntent(context: Context, info: ReportInfo): Intent =
            Intent(context, ReportActivity::class.java).apply {
                putExtra(REPORT_ID_KEY, info)
            }
    }
}
