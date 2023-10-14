package com.ddangddangddang.android.feature.detail.qna.writeanswer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentWriteAnswerDialogBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.util.view.Toaster
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteAnswerDialog : DialogFragment() {
    private var _binding: FragmentWriteAnswerDialogBinding? = null
    private val binding: FragmentWriteAnswerDialogBinding
        get() = binding!!

    private val viewModel: WriteAnswerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.initIds(it.getLong(AUCTION_ID_KEY), it.getLong(QUESTION_ID_KEY))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWriteAnswerDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setupViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupViewModel() {
        viewModel.event.observe(viewLifecycleOwner) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: WriteAnswerViewModel.WriteAnswerEvent) {
        when (event) {
            WriteAnswerViewModel.WriteAnswerEvent.Cancel -> dismiss()
            is WriteAnswerViewModel.WriteAnswerEvent.FailureSubmitAnswer -> {
                notifyFailureMessage(
                    event.errorType,
                    R.string.detail_auction_qna_answer_register_failure,
                )
            }
            WriteAnswerViewModel.WriteAnswerEvent.SubmitAnswer -> {
                notifySuccessMessage()
                dismiss()
            }
        }
    }

    private fun notifySuccessMessage() {
        Toaster.showShort(
            requireContext(),
            getString(R.string.detail_auction_qna_answer_register_success),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        private const val WRITE_ANSWER_TAG = "write_answer_tag"
        private const val AUCTION_ID_KEY = "auction_id"
        private const val QUESTION_ID_KEY = "question_id"

        fun show(fragmentManager: FragmentManager, auctionId: Long, questionId: Long) {
            val dialog = WriteAnswerDialog()
            dialog.arguments =
                Bundle().apply {
                    putLong(AUCTION_ID_KEY, auctionId)
                    putLong(QUESTION_ID_KEY, questionId)
                }
            WriteAnswerDialog().show(fragmentManager, WRITE_ANSWER_TAG)
        }
    }
}
