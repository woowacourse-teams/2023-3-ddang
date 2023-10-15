package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadQuestionDto(
        Long id,
        ReadUserInQnaDto readUserInQnaDto,
        String content,
        LocalDateTime createdTime,
        boolean isDeleted,
        boolean isQuestioner
) {

    private static final boolean IS_NOT_WRITER = false;

    public static ReadQuestionDto of(final Question question, final User user) {
        return new ReadQuestionDto(
                question.getId(),
                ReadUserInQnaDto.from(question.getWriter()),
                question.getContent(),
                question.getCreatedTime(),
                question.isDeleted(),
                isWriter(question, user)
        );
    }

    private static boolean isWriter(final Question question, final User user) {
        if (user == User.EMPTY_USER) {
            return IS_NOT_WRITER;
        }

        return question.isWriter(user);
    }
}
