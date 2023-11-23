package com.ddang.ddang.qna.application.dto.response;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadQnaDto(
        ReadQuestionDto readQuestionDto,
        ReadAnswerDto readAnswerDto
) {

    public static ReadQnaDto of(final Question question, final User user) {
        final ReadQuestionDto readQuestionDto = ReadQuestionDto.of(question, user);
        final ReadAnswerDto readAnswerDto = processReadAnswerDto(question);

        return new ReadQnaDto(readQuestionDto, readAnswerDto);
    }

    private static ReadAnswerDto processReadAnswerDto(final Question question) {
        if (question.getAnswer() == null) {
            return null;
        }

        return ReadAnswerDto.from(question.getAnswer(), question.getAuction().getSeller());
    }

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

    public record ReadAnswerDto(
            Long id,
            ReadUserInQnaDto writerDto,
            String content,
            LocalDateTime createdTime,
            boolean isDeleted
    ) {
        private static ReadAnswerDto from(final Answer answer, final User writer) {
            return new ReadAnswerDto(
                    answer.getId(),
                    ReadUserInQnaDto.from(writer),
                    answer.getContent(),
                    answer.getCreatedTime(),
                    answer.isDeleted()
            );
        }
    }
}
