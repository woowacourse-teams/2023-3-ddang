package com.ddangddangddang.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ReportType : Parcelable {
    ArticleReport, MessageRoomReport
}
