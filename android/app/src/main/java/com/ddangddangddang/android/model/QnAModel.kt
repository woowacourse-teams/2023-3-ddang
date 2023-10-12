package com.ddangddangddang.android.model

data class QnAModel(
    val questionTitle: String,
    val contents: String,
    val answer: String?,
    val status: QnAStatusModel,
    val isOwner: Boolean,
    val isQuestioner: Boolean,
)
