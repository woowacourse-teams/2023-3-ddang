package com.ddangddangddang.android.feature.detail.qna.writequestion

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ddangddangddang.android.databinding.FragmentWriteQuestionDialogBinding
import com.ddangddangddang.android.feature.detail.AuctionDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteQuestionDialog : DialogFragment() {
    private var _binding: FragmentWriteQuestionDialogBinding? = null
    private val binding: FragmentWriteQuestionDialogBinding
        get() = binding!!


//    private val viewModel
    private val activityViewModel: AuctionDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteQuestionDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel
//        binding.activityViewModel = activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
