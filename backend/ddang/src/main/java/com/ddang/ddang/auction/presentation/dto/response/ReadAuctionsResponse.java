package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionCondition;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReadAuctionsResponse(
        List<ReadAuctionResponse> auctions,

        boolean isLast,

        Double lastReliability,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime lastClosingTime,

        Integer lastAuctioneerCount
) {

    public static ReadAuctionsResponse of(
            final ReadAuctionCondition readAuctionCondition,
            final ReadAuctionsDto readAuctionsDto, final String baseUrl) {
        final List<ReadAuctionDto> readAuctionDtos = readAuctionsDto.readAuctionDtos();

        if (readAuctionDtos.isEmpty()) {
            return new ReadAuctionsResponse(Collections.emptyList(), true,  null, null, null);
        }

        final List<ReadAuctionResponse> readAuctionResponses = readAuctionDtos
                                                                              .stream()
                                                                              .map(dto -> ReadAuctionResponse.of(
                                                                                      dto, baseUrl
                                                                              ))
                                                                              .toList();


        return new ReadAuctionsResponse(
                readAuctionResponses,
                readAuctionsDto.isLast(),
                calculateLastReliability(readAuctionCondition, readAuctionsDto),
                calculateLastClosingTime(readAuctionCondition, readAuctionsDto),
                calculateLastAuctioneerCount(readAuctionCondition, readAuctionsDto)
        );
    }

    private static Double calculateLastReliability(
            final ReadAuctionCondition readAuctionCondition,
            final ReadAuctionsDto readAuctionsDto
    ) {
        if (readAuctionsDto.isLast() || !readAuctionCondition.isSortByReliability()) {
            return null;
        }

        final ReadAuctionDto lastReadAuctionDto = findLastReadAuctionDto(readAuctionsDto);

        return lastReadAuctionDto.sellerReliability();
    }


    private static LocalDateTime calculateLastClosingTime(
            final ReadAuctionCondition readAuctionCondition,
            final ReadAuctionsDto readAuctionsDto
    ) {
        if (readAuctionsDto.isLast() || !readAuctionCondition.isSortByClosingTime()) {
            return null;
        }

        final ReadAuctionDto lastReadAuctionDto = findLastReadAuctionDto(readAuctionsDto);

        return lastReadAuctionDto.closingTime();
    }

    private static ReadAuctionDto findLastReadAuctionDto(final ReadAuctionsDto readAuctionsDto) {
        final List<ReadAuctionDto> readAuctionDtos = readAuctionsDto.readAuctionDtos();

        return readAuctionDtos.get(readAuctionDtos.size() - 1);
    }

    private static Integer calculateLastAuctioneerCount(
            final ReadAuctionCondition readAuctionCondition,
            final ReadAuctionsDto readAuctionsDto
    ) {
        if (readAuctionsDto.isLast() || !readAuctionCondition.isSortByAuctioneerCount()) {
            return null;
        }

        final ReadAuctionDto lastReadAuctionDto = findLastReadAuctionDto(readAuctionsDto);

        return lastReadAuctionDto.auctioneerCount();
    }
}
