package com.ddangddangddang.android.feature.detail.qna

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentQnaBinding
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QnaFragment : BindingFragment<FragmentQnaBinding>(R.layout.fragment_qna) {
    private val qnaAdapter = QnaAdapter { qnaId ->
        // viewModel.selectQna~
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvQna.adapter = qnaAdapter
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("changed", "change")
    }
}
