package com.ddangddangddang.android.feature.detail.qna

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.ddangddangddang.android.R
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
        if (qna.isPicked) {
            binding.tvQuestionTitle.setTextColor(binding.root.context.getColor(R.color.selected_second_region_text))
            binding.tvQuestionTitle.typeface = Typeface.create(ResourcesCompat.getFont(binding.root.context, R.font.pretendard), 700, false)
        } else {
            binding.tvQuestionTitle.setTextColor(binding.root.context.getColor(R.color.grey_700))
            binding.tvQuestionTitle.typeface = Typeface.create(ResourcesCompat.getFont(binding.root.context, R.font.pretendard), 400, false)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onClicks: QnaAdapter.OnClicks): QnaViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemQnaBinding.inflate(layoutInflater, parent, false)
            return QnaViewHolder(binding, onClicks)
        }
    }
}
