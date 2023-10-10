package com.ddang.ddang.qna.application.event;

import com.ddang.ddang.qna.domain.Answer;

public record AnswerNotificationEvent(Answer answer) {
}
