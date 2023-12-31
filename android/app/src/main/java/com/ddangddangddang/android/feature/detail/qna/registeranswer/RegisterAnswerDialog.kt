package com.ddangddangddang.android.feature.detail.qna.registeranswer

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
import com.ddangddangddang.android.databinding.FragmentRegisterAnswerDialogBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.detail.qna.QnaViewModel
import com.ddangddangddang.android.util.view.Toaster
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterAnswerDialog : DialogFragment() {
    private var _binding: FragmentRegisterAnswerDialogBinding? = null
    private val binding: FragmentRegisterAnswerDialogBinding
        get() = _binding!!

    private val viewModel: RegisterAnswerViewModel by viewModels()
    private val parentViewModel: QnaViewModel by viewModels(ownerProducer = { requireParentFragment() })

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
    ): View {
        _binding = FragmentRegisterAnswerDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupHeight()
        setupViewModel()
    }

    private fun setupHeight() {
        val screenHeight = requireContext().resources?.displayMetrics?.heightPixels ?: return

        val params: ViewGroup.LayoutParams? = binding.etAnswerContents.layoutParams
        val height = params?.height ?: return
        val newHeight = (screenHeight * 0.35).toInt()
        if (newHeight > height) {
            params.height = newHeight
            binding.etAnswerContents.layoutParams = params
        }
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
        viewModel.event.observe(viewLifecycleOwner) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: RegisterAnswerViewModel.WriteAnswerEvent) {
        when (event) {
            RegisterAnswerViewModel.WriteAnswerEvent.Cancel -> {
                dismiss()
            }

            is RegisterAnswerViewModel.WriteAnswerEvent.FailureSubmitAnswer -> {
                notifyFailureMessage(
                    event.errorType,
                    R.string.detail_auction_qna_answer_register_failure,
                )
            }

            RegisterAnswerViewModel.WriteAnswerEvent.SubmitAnswer -> {
                notifySuccessMessage()
                parentViewModel.loadQnas()
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

    companion object {
        private const val WRITE_ANSWER_TAG = "write_answer_tag"
        private const val AUCTION_ID_KEY = "auction_id"
        private const val QUESTION_ID_KEY = "question_id"

        fun show(
            fragmentManager: FragmentManager,
            auctionId: Long,
            questionId: Long,
        ) {
            val dialog = RegisterAnswerDialog()
            dialog.arguments =
                Bundle().apply {
                    putLong(AUCTION_ID_KEY, auctionId)
                    putLong(QUESTION_ID_KEY, questionId)
                }
            dialog.show(fragmentManager, WRITE_ANSWER_TAG)
        }
    }
}
