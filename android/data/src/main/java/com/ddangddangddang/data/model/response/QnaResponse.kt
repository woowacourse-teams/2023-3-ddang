package com.ddangddangddang.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class QnaResponse(val qnas: List<Qna>) {
    @Serializable
    data class Qna(val question: Question, val answer: Answer?)

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
