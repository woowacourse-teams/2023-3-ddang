package com.ddangddangddang.android.feature.detail.qna.registerquestion

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
import com.ddangddangddang.android.databinding.FragmentRegisterQuestionDialogBinding
import com.ddangddangddang.android.feature.common.notifyFailureMessage
import com.ddangddangddang.android.feature.detail.qna.QnaViewModel
import com.ddangddangddang.android.util.view.Toaster
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterQuestionDialog : DialogFragment() {
    private var _binding: FragmentRegisterQuestionDialogBinding? = null
    private val binding: FragmentRegisterQuestionDialogBinding
        get() = _binding!!

    private val viewModel: RegisterQuestionViewModel by viewModels()
    private val parentViewModel: QnaViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.initAuctionId(it.getLong(AUCTION_ID_KEY))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterQuestionDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun notifySuccessMessage() {
        Toaster.showShort(
            requireContext(),
            getString(R.string.detail_auction_qna_question_register_success),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupHeight()
        setupViewModel()
    }

    private fun setupHeight() {
        val screenHeight = requireContext().resources?.displayMetrics?.heightPixels ?: return

        val params: ViewGroup.LayoutParams? = binding.etQuestionContents.layoutParams
        val height = params?.height ?: return
        val newHeight = (screenHeight * 0.35).toInt()
        if (newHeight > height) {
            params.height = newHeight
            binding.etQuestionContents.layoutParams = params
        }
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
        viewModel.event.observe(viewLifecycleOwner) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: RegisterQuestionViewModel.WriteQuestionEvent) {
        when (event) {
            RegisterQuestionViewModel.WriteQuestionEvent.Cancel -> {
                dismiss()
            }

            is RegisterQuestionViewModel.WriteQuestionEvent.FailureSubmitQuestion -> {
                notifyFailureMessage(
                    event.errorType,
                    R.string.detail_auction_qna_question_register_failure,
                )
            }

            RegisterQuestionViewModel.WriteQuestionEvent.SubmitQuestion -> {
                notifySuccessMessage()
                parentViewModel.loadQnas()
                dismiss()
            }
        }
    }

    companion object {
        private const val WRITE_QUESTION_TAG = "write_question_tag"
        private const val AUCTION_ID_KEY = "auction_id"

        fun show(fragmentManager: FragmentManager, auctionId: Long) {
            val dialog = RegisterQuestionDialog()
            dialog.arguments = Bundle().apply {
                putLong(AUCTION_ID_KEY, auctionId)
            }
            dialog.show(fragmentManager, WRITE_QUESTION_TAG)
        }
    }
}
