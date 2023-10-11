package com.ddang.ddang.qna.application.event;

import com.ddang.ddang.qna.domain.Question;

public record QuestionNotificationEvent(Question question, String absoluteImageUrl) {
}
