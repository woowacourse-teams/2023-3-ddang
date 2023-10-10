package com.ddang.ddang.qna.application.event;

import com.ddang.ddang.qna.application.dto.QuestionDto;

public record QuestionNotificationEvent(QuestionDto questionDto) {
}
