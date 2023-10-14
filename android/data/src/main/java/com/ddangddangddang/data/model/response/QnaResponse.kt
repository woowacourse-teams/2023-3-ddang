package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class QnaResponse(val questionAndAnswers: List<QuestionAndAnswer>) {
    @Serializable
    data class QuestionAndAnswer(val question: Question, val answer: Answer?)

    @Serializable
    data class Question(
        val id: Long,
        val writer: Writer,
        val createdTime: String,
        val content: String,
        val isQuestioner: Boolean,
    )

    @Serializable
    data class Answer(
        val id: Long,
        val writer: Writer,
        val createdTime: String,
        val content: String,
    )

    @Serializable
    data class Writer(val id: Long, val name: String, val image: String)
}
