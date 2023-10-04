package com.ddangddangddang.android.feature.messageRoom.rate

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ddangddangddang.android.databinding.FragmentUserRateDialogBinding
import com.ddangddangddang.android.feature.messageRoom.MessageRoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRateDialog : DialogFragment() {
    private var _binding: FragmentUserRateDialogBinding? = null
    private val binding: FragmentUserRateDialogBinding
        get() = _binding!!

    private val viewModel: UserRateViewModel by viewModels()
    private val activityViewModel: MessageRoomViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPartnerInfo()
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
        _binding = FragmentUserRateDialogBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@UserRateDialog
                viewModel = this@UserRateDialog.viewModel
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupListener()
    }

    private fun setupListener() {
        binding.btnRateCancel.setOnClickListener { exit() }
    }

    private fun exit() {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_RATE_DIALOG_TAG = "user_rate_dialog_tag"
    }
}
