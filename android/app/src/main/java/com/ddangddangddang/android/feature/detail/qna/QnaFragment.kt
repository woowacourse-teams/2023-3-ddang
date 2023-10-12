package com.ddangddangddang.android.feature.detail.qna

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.FragmentQnaBinding
import com.ddangddangddang.android.util.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QnaFragment : BindingFragment<FragmentQnaBinding>(R.layout.fragment_qna) {

    private val viewModel: QnaViewModel by viewModels()
    private val qnaAdapter = QnaAdapter { qnaId ->
        viewModel.selectQna(qnaId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvQna.adapter = qnaAdapter
        binding.rvQna.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.qnas.observe(viewLifecycleOwner) {
            qnaAdapter.setQnas(it)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("changed", "change")
    }
}
