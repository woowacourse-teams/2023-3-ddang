package com.ddangddangddang.android.feature.detail.qna.writequestion

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.databinding.FragmentWriteQuestionDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteQuestionDialog : DialogFragment() {
    private var _binding: FragmentWriteQuestionDialogBinding? = null
    private val binding: FragmentWriteQuestionDialogBinding
        get() = binding!!

    private val viewModel: WriteQuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWriteQuestionDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        private const val WRITE_QUESTION_TAG = "write_question_tag"
        private const val AUCTION_ID_KEY = "auction_id"

        fun show(fragmentManager: FragmentManager, auctionId: Long) {
            val dialog = WriteQuestionDialog()
            dialog.arguments = Bundle().apply {
                putLong(AUCTION_ID_KEY, auctionId)
            }
            WriteQuestionDialog().show(fragmentManager, WRITE_QUESTION_TAG)
        }
    }
}
