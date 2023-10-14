package com.ddangddangddang.android.model

import java.io.Serializable

sealed class ReportInfo : Serializable {
    data class ArticleReportInfo(val auctionId: Long) : ReportInfo()
    data class MessageRoomReportInfo(val roomId: Long) : ReportInfo()
    data class QuestionReportInfo(val auctionId: Long, val questionId: Long) : ReportInfo()
    data class AnswerReportInfo(val auctionId: Long, val questionId: Long, val answerId: Long) : ReportInfo()
}
