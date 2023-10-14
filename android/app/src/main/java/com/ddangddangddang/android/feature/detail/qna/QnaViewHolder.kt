package com.ddangddangddang.android.feature.detail.qna

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemQnaBinding
import com.ddangddangddang.android.model.QnaModel

class QnaViewHolder private constructor(
    private val binding: ItemQnaBinding,
    onClicks: QnaAdapter.OnClicks,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onClicks = onClicks
    }

    fun bind(qna: QnaModel.QuestionAndAnswerModel) {
        binding.model = qna
    }

    companion object {
        fun create(parent: ViewGroup, onClicks: QnaAdapter.OnClicks): QnaViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemQnaBinding.inflate(layoutInflater, parent, false)
            return QnaViewHolder(binding, onClicks)
        }
    }
}
