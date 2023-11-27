package com.ddang.ddang.qna.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto.AnswerDto.AnswererInfoDto;
import com.ddang.ddang.qna.application.dto.response.ReadMultipleQnaDto.QnaInfoDto.QuestionDto.QuestionerInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadMultipleQnaResponse(List<QnaInfoResponse> qnas) {

    public static ReadMultipleQnaResponse of(final ReadMultipleQnaDto readMultipleQnaDto, final String imageRelativeUrl) {
        final List<QnaInfoDto> qnaInfoDtos = readMultipleQnaDto.qnaInfoDtos();
        final List<QnaInfoResponse> readSingleQnaRespons = qnaInfoDtos.stream()
                                                                      .map(dto -> QnaInfoResponse.of(dto, imageRelativeUrl))
                                                                      .toList();

        return new ReadMultipleQnaResponse(readSingleQnaRespons);
    }

    public record QnaInfoResponse(
            QuestionInfoResponse question,
            AnswerInfoResponse answer
    ) {

        public static QnaInfoResponse of(final QnaInfoDto qnaInfoDto, final String imageRelativeUrl) {
            final QuestionInfoResponse question = QuestionInfoResponse.of(qnaInfoDto, imageRelativeUrl);
            final AnswerInfoResponse answer = processReadAnswerResponse(qnaInfoDto, imageRelativeUrl);

            return new QnaInfoResponse(question, answer);
        }

        private static AnswerInfoResponse processReadAnswerResponse(
                final QnaInfoDto qnaInfoDto,
                final String imageRelativeUrl
        ) {
            if (qnaInfoDto.answerDto() == null || qnaInfoDto.answerDto().isDeleted()) {
                return null;
            }

            return AnswerInfoResponse.of(qnaInfoDto, imageRelativeUrl);
        }

        public record AnswerInfoResponse(
                Long id,
                AnswererInfoResponse writer,
                LocalDateTime createdTime,
                String content
        ) {
            private static AnswerInfoResponse of(final QnaInfoDto qnaInfoDto, final String imageRelativeUrl) {
                return new AnswerInfoResponse(
                        qnaInfoDto.answerDto().id(),
                        AnswererInfoResponse.of(qnaInfoDto.answerDto().writerDto(), imageRelativeUrl),
                        qnaInfoDto.answerDto().createdTime(),
                        qnaInfoDto.answerDto().content()
                );
            }

            public record AnswererInfoResponse(Long id, String name, String image) {

                private static AnswererInfoResponse of(final AnswererInfoDto dto, final String imageUrl) {
                    return new AnswererInfoResponse(
                            dto.id(),
                            dto.name(),
                            imageUrl + dto.profileImageStoreName()
                    );
                }
            }
        }

        public record QuestionInfoResponse(
                Long id,
                QuestionerInfoResponse writer,
                LocalDateTime createdTime,
                String content,
                boolean isQuestioner
        ) {

            private static QuestionInfoResponse of(
                    final QnaInfoDto qnaInfoDto,
                    final String imageRelativeUrl
            ) {
                return new QuestionInfoResponse(
                        qnaInfoDto.questionDto().id(),
                        QuestionerInfoResponse.of(qnaInfoDto.questionDto().questionerInfoDto(), imageRelativeUrl),
                        qnaInfoDto.questionDto().createdTime(),
                        qnaInfoDto.questionDto().content(),
                        qnaInfoDto.questionDto().isQuestioner()
                );
            }
        }

        public record QuestionerInfoResponse(Long id, String name, String image) {

            private static QuestionerInfoResponse of(final QuestionerInfoDto dto, final String imageUrl) {
                return new QuestionerInfoResponse(
                        dto.id(),
                        dto.name(),
                        imageUrl + dto.profileImageStoreName()
                );
            }
        }
    }
}
