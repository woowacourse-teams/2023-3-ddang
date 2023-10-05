package com.ddangddangddang.android.feature.messageRoom.review

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentUserReviewDialogBinding
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.feature.messageRoom.MessageRoomViewModel
import com.ddangddangddang.android.util.view.Toaster
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserReviewDialog : DialogFragment() {
    private var _binding: FragmentUserReviewDialogBinding? = null
    private val binding: FragmentUserReviewDialogBinding
        get() = _binding!!

    private val viewModel: UserReviewViewModel by viewModels()
    private val activityViewModel: MessageRoomViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()
        loadPartnerInfo()
    }

    private fun setupObserve() {
        viewModel.event.observe(this) { event ->
            handleEvent(event)
        }
    }

    private fun handleEvent(event: UserReviewViewModel.ReviewEvent) {
        when (event) {
            UserReviewViewModel.ReviewEvent.ReviewSuccess -> {
                notifySubmitSuccess()
                exit()
            }
            is UserReviewViewModel.ReviewEvent.ReviewFailure -> {
                notifySubmitFailure(event.error)
            }
        }
    }

    private fun notifySubmitSuccess() {
        Toaster.showShort(requireContext(), getString(R.string.user_review_success))
    }

    private fun notifySubmitFailure(errorType: ErrorType) {
        val defaultMessage = getString(R.string.user_review_failure)
        val message = when (errorType) {
            is ErrorType.FAILURE -> errorType.message
            is ErrorType.NETWORK_ERROR -> getString(errorType.messageId)
            is ErrorType.UNEXPECTED -> getString(errorType.messageId)
        }
        Toaster.showShort(requireContext(), message ?: defaultMessage)
    }

    private fun loadPartnerInfo() {
        activityViewModel.messageRoomInfo.value?.let { info ->
            viewModel.setPartnerInfo(info)
            return
        }
        exit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserReviewDialogBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@UserReviewDialog
                viewModel = this@UserReviewDialog.viewModel
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupListener()
    }

    private fun setupListener() {
        binding.btnReviewCancel.setOnClickListener { exit() }
    }

    private fun exit() {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val USER_REVIEW_DIALOG_TAG = "user_review_dialog_tag"

        fun show(fragmentManager: FragmentManager) {
            UserReviewDialog().show(fragmentManager, USER_REVIEW_DIALOG_TAG)
        }
    }
}
