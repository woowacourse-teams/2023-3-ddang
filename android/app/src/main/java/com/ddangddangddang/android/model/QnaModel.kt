package com.ddangddangddang.android.model

data class QnaModel(val questionAndAnswers: List<QuestionAndAnswer>) {
    data class QuestionAndAnswer(
        val id: Long,
        val questionTitle: String,
        val contents: String,
        val answer: String?,
        val status: QnaStatusModel,
        val isOwner: Boolean,
        val isQuestioner: Boolean,
        val isPicked: Boolean,
    )
}
