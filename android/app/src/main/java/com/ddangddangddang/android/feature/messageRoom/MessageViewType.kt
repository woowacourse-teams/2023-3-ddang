package com.ddangddangddang.android.feature.messageRoom

import androidx.annotation.LayoutRes
import com.ddangddangddang.android.R

enum class MessageViewType(@LayoutRes val id: Int) {
    MY_MESSAGE(R.layout.item_my_message),
    PARTNER_MESSAGE(R.layout.item_partner_message),
}
