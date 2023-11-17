package com.ddang.ddang.chat.application.event;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.user.domain.User;

public record UpdateReadMessageLogEvent(User reader, ChatRoom chatRoom, Message lastReadMessage) {
}
