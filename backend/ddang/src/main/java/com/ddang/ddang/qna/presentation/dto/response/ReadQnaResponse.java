package com.ddang.ddang.qna.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.response.ReadQnaDto;
import com.ddang.ddang.qna.application.dto.response.ReadUserInQnaDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadQnaResponse(
        ReadQuestionResponse question,
        ReadAnswerResponse answer
) {

    public static ReadQnaResponse of(final ReadQnaDto readQnaDto, final String imageRelativeUrl) {
        final ReadQuestionResponse question = ReadQuestionResponse.of(readQnaDto, imageRelativeUrl);
        final ReadAnswerResponse answer = processReadAnswerResponse(readQnaDto, imageRelativeUrl);

        return new ReadQnaResponse(question, answer);
    }

    private static ReadAnswerResponse processReadAnswerResponse(
            final ReadQnaDto readQnaDto,
            final String imageRelativeUrl
    ) {
        if (readQnaDto.readAnswerDto() == null || readQnaDto.readAnswerDto().isDeleted()) {
            return null;
        }

        return ReadAnswerResponse.of(readQnaDto, imageRelativeUrl);
    }

    public record ReadAnswerResponse(
            Long id,

            ReadUserInAuctionQuestionResponse writer,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            String content
    ) {
        private static ReadAnswerResponse of(final ReadQnaDto readQnaDto, final String imageRelativeUrl) {
            return new ReadAnswerResponse(
                    readQnaDto.readAnswerDto().id(),
                    ReadUserInAuctionQuestionResponse.of(readQnaDto.readAnswerDto().writerDto(), imageRelativeUrl),
                    readQnaDto.readAnswerDto().createdTime(),
                    readQnaDto.readAnswerDto().content()
            );
        }
    }

    public record ReadQuestionResponse(
            Long id,

            ReadUserInAuctionQuestionResponse writer,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            String content,

            boolean isQuestioner
    ) {

        private static ReadQuestionResponse of(
                final ReadQnaDto readQnaDto,
                final String imageRelativeUrl
        ) {
            return new ReadQuestionResponse(
                    readQnaDto.readQuestionDto().id(),
                    ReadUserInAuctionQuestionResponse.of(readQnaDto.readQuestionDto().readUserInQnaDto(), imageRelativeUrl),
                    readQnaDto.readQuestionDto().createdTime(),
                    readQnaDto.readQuestionDto().content(),
                    readQnaDto.readQuestionDto().isQuestioner()
            );
        }
    }

    public record ReadUserInAuctionQuestionResponse(Long id, String name, String image) {

        private static ReadUserInAuctionQuestionResponse of(final ReadUserInQnaDto dto, final String imageUrl) {
            return new ReadUserInAuctionQuestionResponse(
                    dto.id(),
                    dto.name(),
                    imageUrl + dto.profileImageStoreName()
            );
        }
    }
}
