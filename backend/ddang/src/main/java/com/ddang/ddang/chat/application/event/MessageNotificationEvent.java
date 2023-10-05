package com.ddang.ddang.chat.application.event;

import com.ddang.ddang.chat.application.dto.MessageDto;

public record MessageNotificationEvent(MessageDto messageDto) {
}
