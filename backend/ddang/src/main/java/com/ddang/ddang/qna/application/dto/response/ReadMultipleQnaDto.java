package com.ddang.ddang.qna.application.dto.response;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public record ReadMultipleQnaDto(List<QnaInfoDto> qnaInfoDtos) {

    public static ReadMultipleQnaDto of(final List<Question> questions, final User user) {
        final List<QnaInfoDto> qnaInfoDtos = questions.stream()
                                                      .map(question -> QnaInfoDto.of(question, user))
                                                      .toList();

        return new ReadMultipleQnaDto(qnaInfoDtos);
    }

    public record QnaInfoDto(
            QuestionDto questionDto,
            AnswerDto answerDto
    ) {

        public static QnaInfoDto of(final Question question, final User user) {
            final QuestionDto questionDto = QuestionDto.of(question, user);
            final AnswerDto answerDto = processReadAnswerDto(question);

            return new QnaInfoDto(questionDto, answerDto);
        }

        private static AnswerDto processReadAnswerDto(final Question question) {
            if (question.getAnswer() == null) {
                return null;
            }

            return AnswerDto.from(question.getAnswer(), question.getAuction().getSeller());
        }

        public record QuestionDto(
                Long id,
                QuestionerInfoDto questionerInfoDto,
                String content,
                LocalDateTime createdTime,
                boolean isDeleted,
                boolean isQuestioner
        ) {
            private static final boolean IS_NOT_WRITER = false;

            public static QuestionDto of(final Question question, final User user) {
                return new QuestionDto(
                        question.getId(),
                        QuestionerInfoDto.from(question.getWriter()),
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

            public record QuestionerInfoDto(
                    Long id,
                    String name,
                    String profileImageStoreName,
                    double reliability,
                    String oauthId,
                    boolean isDeleted
            ) {
                public static QuestionerInfoDto from(final User writer) {
                    return new QuestionerInfoDto(
                            writer.getId(),
                            writer.findName(),
                            writer.getProfileImage().getStoreName(),
                            writer.getReliability().getValue(),
                            writer.getOauthInformation().getOauthId(),
                            writer.isDeleted()
                    );
                }
            }
        }

        public record AnswerDto(
                Long id,
                AnswererInfoDto writerDto,
                String content,
                LocalDateTime createdTime,
                boolean isDeleted
        ) {
            private static AnswerDto from(final Answer answer, final User writer) {
                return new AnswerDto(
                        answer.getId(),
                        AnswererInfoDto.from(writer),
                        answer.getContent(),
                        answer.getCreatedTime(),
                        answer.isDeleted()
                );
            }

            public record AnswererInfoDto(
                    Long id,
                    String name,
                    String profileImageStoreName,
                    double reliability,
                    String oauthId,
                    boolean isDeleted
            ) {
                public static AnswererInfoDto from(final User writer) {
                    return new AnswererInfoDto(
                            writer.getId(),
                            writer.findName(),
                            writer.getProfileImage().getStoreName(),
                            writer.getReliability().getValue(),
                            writer.getOauthInformation().getOauthId(),
                            writer.isDeleted()
                    );
                }
            }
        }
    }
}
