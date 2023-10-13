package com.ddangddangddang.android.feature.detail.qna

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ddangddangddang.android.model.QnaModel

class QnaAdapter(private val onItemClick: (Long) -> Unit) :
    ListAdapter<QnaModel.QuestionAndAnswer, QnaViewHolder>(QnaDiffUtil) {

    fun setQnas(list: List<QnaModel.QuestionAndAnswer>, callback: (() -> Unit)? = null) {
        submitList(list, callback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnaViewHolder {
        return QnaViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: QnaViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val QnaDiffUtil = object : DiffUtil.ItemCallback<QnaModel.QuestionAndAnswer>() {
            override fun areItemsTheSame(
                oldItem: QnaModel.QuestionAndAnswer,
                newItem: QnaModel.QuestionAndAnswer,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: QnaModel.QuestionAndAnswer,
                newItem: QnaModel.QuestionAndAnswer,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
