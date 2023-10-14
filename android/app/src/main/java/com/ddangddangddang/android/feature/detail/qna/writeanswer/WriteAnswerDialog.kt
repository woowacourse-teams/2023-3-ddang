package com.ddangddangddang.android.feature.detail.qna.writeanswer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.ddangddangddang.android.databinding.FragmentWriteAnswerDialogBinding
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteAnswerDialog : DialogFragment() {
    private var _binding: FragmentWriteAnswerDialogBinding? = null
    private val binding: FragmentWriteAnswerDialogBinding
        get() = binding!!

    //    private val viewModel
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWriteAnswerDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel
//        binding.activityViewModel = activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
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
