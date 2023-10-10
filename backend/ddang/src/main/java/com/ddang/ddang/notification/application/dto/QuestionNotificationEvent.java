package com.ddang.ddang.notification.application.dto;

import com.ddang.ddang.qna.application.dto.QuestionDto;

public record QuestionNotificationEvent(QuestionDto questionDto) {
}
