package com.ddang.ddang.qna.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadAnswerDto;
import com.ddang.ddang.qna.application.dto.ReadQnaDto;
import com.ddang.ddang.qna.application.dto.ReadQuestionDto;
import com.ddang.ddang.qna.application.dto.ReadUserInQnaDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadQnaResponse(
        ReadQuestionResponse question,
        ReadAnswerResponse answer
) {

    public static ReadQnaResponse of(final ReadQnaDto readQnaDto, final String imageRelativeUrl) {
        final ReadQuestionResponse question = ReadQuestionResponse.of(readQnaDto.readQuestionDto(), imageRelativeUrl);
        final ReadAnswerResponse answer = processReadAnswerResponse(readQnaDto.readAnswerDto(), imageRelativeUrl);

        return new ReadQnaResponse(question, answer);
    }

    private static ReadAnswerResponse processReadAnswerResponse(
            final ReadAnswerDto readAnswerDto,
            final String imageRelativeUrl
    ) {
        if (readAnswerDto == null || readAnswerDto.isDeleted()) {
            return null;
        }

        return ReadAnswerResponse.of(readAnswerDto, imageRelativeUrl);
    }

    private record ReadAnswerResponse(
            Long id,

            ReadUserInAuctionQuestionResponse writer,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            String content
    ) {
        public static ReadAnswerResponse of(final ReadAnswerDto readAnswerDto, final String imageRelativeUrl) {
            return new ReadAnswerResponse(
                    readAnswerDto.id(),
                    ReadUserInAuctionQuestionResponse.of(readAnswerDto.writerDto(), imageRelativeUrl),
                    readAnswerDto.createdTime(),
                    readAnswerDto.content()
            );
        }
    }

    private record ReadQuestionResponse(
            Long id,

            ReadUserInAuctionQuestionResponse writer,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            String content,

            boolean isQuestioner
    ) {

        public static ReadQuestionResponse of(
                final ReadQuestionDto readQuestionDto,
                final String imageRelativeUrl
        ) {
            return new ReadQuestionResponse(
                    readQuestionDto.id(),
                    ReadUserInAuctionQuestionResponse.of(readQuestionDto.readUserInQnaDto(), imageRelativeUrl),
                    readQuestionDto.createdTime(),
                    readQuestionDto.content(),
                    readQuestionDto.isQuestioner()
            );
        }
    }

    private record ReadUserInAuctionQuestionResponse(Long id, String name, String image) {

        private static ReadUserInAuctionQuestionResponse of(final ReadUserInQnaDto dto, final String imageUrl) {
            return new ReadUserInAuctionQuestionResponse(
                    dto.id(),
                    dto.name(),
                    imageUrl + dto.profileImageStoreName()
            );
        }
    }
}
