package com.ddangddangddang.android.feature.detail.qna

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.QnAModel

class QnaAdapter(private val onItemClick: (Long) -> Unit) :
    ListAdapter<QnAModel.QuestionAndAnswer, QnaViewHolder>(QnaDiffUtil) {

    fun setQnas(list: List<QnAModel.QuestionAndAnswer>, callback: (() -> Unit)? = null) {
        submitList(list, callback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnaViewHolder {
        return QnaViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: QnaViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val QnaDiffUtil = object : DiffUtil.ItemCallback<QnAModel.QuestionAndAnswer>() {
            override fun areItemsTheSame(
                oldItem: QnAModel.QuestionAndAnswer,
                newItem: QnAModel.QuestionAndAnswer,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: QnAModel.QuestionAndAnswer,
                newItem: QnAModel.QuestionAndAnswer,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
