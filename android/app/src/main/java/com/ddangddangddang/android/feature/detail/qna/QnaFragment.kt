package com.ddangddangddang.android.feature.detail.qna

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentQnaBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import com.ddangddangddang.android.feature.detail.qna.registeranswer.RegisterAnswerDialog
import com.ddangddangddang.android.feature.detail.qna.registerquestion.RegisterQuestionDialog
import com.ddangddangddang.android.feature.report.ReportActivity
import com.ddangddangddang.android.model.ReportInfo
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QnaFragment : BindingFragment<FragmentQnaBinding>(R.layout.fragment_qna) {

    private val viewModel: QnaViewModel by viewModels()
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()
    private val onClicks = object : QnaAdapter.OnClicks {
        override fun onQuestionClick(questionId: Long) {
            viewModel.selectQna(questionId)
        }

        override fun onSubmitAnswerClick(questionId: Long) {
            activityViewModel.auctionDetailModel.value?.let { model ->
                RegisterAnswerDialog.show(
                    childFragmentManager,
                    model.id,
                    questionId,
                )
            }
        }

        override fun onDeleteQuestionClick(questionId: Long) {
            viewModel.deleteQuestion(questionId)
        }

        override fun onDeleteAnswerClick(answerId: Long) {
            viewModel.deleteAnswer(answerId)
        }

        override fun onReportQuestionClick(questionId: Long) {
            viewModel.reportQuestion(questionId)
        }

        override fun onReportAnswerClick(questionId: Long, answerId: Long) {
            viewModel.reportAnswer(questionId, answerId)
        }
    }
    private val qnaAdapter = QnaAdapter(onClicks)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupQnas()
        setupViewModel()
        setupBinding()
    }

    private fun setupQnas() {
        binding.rvQna.adapter = qnaAdapter
        binding.rvQna.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
    }

    private fun setupViewModel() {
        activityViewModel.auctionDetailModel.value?.let {
            viewModel.initAuctionInfo(it.isOwner, it.id)
            viewModel.loadQnas()
        }

        viewModel.qnas.observe(viewLifecycleOwner) {
            qnaAdapter.setQnas(it)
        }
        viewModel.event.observe(viewLifecycleOwner) { event ->
            handleEvent(event)
        }
    }

    private fun handleEvent(event: QnaViewModel.QnaEvent) {
        when (event) {
            is QnaViewModel.QnaEvent.FailureLoadQnas -> {
                notifyFailureMessage(event.errorType, R.string.detail_auction_qna_loading_failure)
            }

            is QnaViewModel.QnaEvent.FailureDeleteAnswer -> {
                notifyFailureMessage(
                    event.errorType,
                    R.string.detail_auction_qna_answer_delete_failure,
                )
            }

            is QnaViewModel.QnaEvent.FailureDeleteQuestion -> {
                notifyFailureMessage(
                    event.errorType,
                    R.string.detail_auction_qna_question_delete_failure,
                )
            }

            is QnaViewModel.QnaEvent.ReportQuestion -> {
                navigateToReport(event.info)
            }

            is QnaViewModel.QnaEvent.ReportAnswer -> {
                navigateToReport(event.info)
            }
        }
    }

    private fun navigateToReport(info: ReportInfo) {
        startActivity(ReportActivity.getIntent(requireContext(), info))
    }

    private fun setupBinding() {
        binding.clWriteQuestion.setOnClickListener {
            activityViewModel.auctionDetailModel.value?.let { model ->
                RegisterQuestionDialog.show(
                    childFragmentManager,
                    model.id,
                )
            }
        }
        activityViewModel.auctionDetailModel.value?.let {
            binding.isOwner = it.isOwner
        }
    }
}
