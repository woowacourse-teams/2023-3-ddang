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
import com.ddangddangddang.android.feature.detail.qna.writeanswer.WriteAnswerDialog
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
                WriteAnswerDialog.show(parentFragmentManager, model.id, questionId)
            }
        }

        override fun onDeleteQuestionClick(questionId: Long) {
            viewModel.deleteQuestion(questionId)
        }

        override fun onDeleteAnswerClick(answerId: Long) {
            viewModel.deleteAnswer(answerId)
        }
    }
    private val qnaAdapter = QnaAdapter(onClicks)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupQnas()
        setupViewModel()
    }

    private fun setupQnas() {
        binding.rvQna.adapter = qnaAdapter
        binding.rvQna.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
    }

    private fun setupViewModel() {
        activityViewModel.auctionDetailModel.value?.let {
            viewModel.initIsOwner(it.isOwner)
            viewModel.loadQnas(it.id)
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
                notifyFailureMessage(event.errorType, R.string.detail_auction_qna_answer_delete_failure)
            }
            is QnaViewModel.QnaEvent.FailureDeleteQuestion -> {
                notifyFailureMessage(event.errorType, R.string.detail_auction_qna_question_delete_failure)
            }
        }
    }
}
