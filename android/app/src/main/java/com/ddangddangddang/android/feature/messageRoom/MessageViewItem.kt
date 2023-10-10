package com.ddangddangddang.android.feature.messageRoom

sealed interface MessageViewItem {
    val id: Long
    val type: MessageViewType
    val createdDateTime: String
    val contents: String

    data class MyMessageViewItem(
        override val id: Long,
        override val createdDateTime: String,
        override val contents: String,
    ) : MessageViewItem {
        override val type: MessageViewType = MessageViewType.MY_MESSAGE
    }

    data class PartnerMessageViewItem(
        override val id: Long,
        override val createdDateTime: String,
        override val contents: String,
    ) : MessageViewItem {
        override val type: MessageViewType = MessageViewType.PARTNER_MESSAGE
    }
}
