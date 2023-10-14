package com.ddangddangddang.android.feature.detail.qna

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.databinding.ItemQnaBinding
import com.ddangddangddang.android.model.QnaModel

class QnaViewHolder private constructor(
    private val binding: ItemQnaBinding,
    onItemClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.onItemClick = onItemClick
    }

    fun bind(qna: QnaModel.QuestionAndAnswerModel) {
        binding.model = qna
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Long) -> Unit): QnaViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemQnaBinding.inflate(layoutInflater, parent, false)
            return QnaViewHolder(binding, onItemClick)
        }
    }
}
