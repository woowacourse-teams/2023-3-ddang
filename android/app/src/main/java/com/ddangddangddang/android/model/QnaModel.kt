package com.ddangddangddang.android.model

data class QnaModel(val questionAndAnswers: List<QuestionAndAnswerModel>) {
    data class QuestionAndAnswerModel(
        val question: QuestionModel,
        val answer: AnswerModel?,
        val isOwner: Boolean,
        val isPicked: Boolean,
        val status: QnaStatusModel,
    )

    data class QuestionModel(
        val id: Long,
        val createdTime: String,
        val content: String,
        val isQuestioner: Boolean,
    )

    data class AnswerModel(
        val id: Long,
        val createdTime: String,
        val content: String,
    )
}
