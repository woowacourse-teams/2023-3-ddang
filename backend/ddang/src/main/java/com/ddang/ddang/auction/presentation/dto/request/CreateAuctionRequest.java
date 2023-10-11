package com.ddang.ddang.auction.presentation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public record CreateAuctionRequest(
        @NotEmpty(message = "제목이 입력되지 않았습니다.")
        String title,

        @NotEmpty(message = "설명이 입력되지 않았습니다.")
        String description,

        @NotNull(message = "입찰 단위가 입력되지 않았습니다.")
        @Positive(message = "금액은 양수로 입력해주세요.")
        Integer bidUnit,

        @NotNull(message = "시작가가 입력되지 않았습니다.")
        @Positive(message = "금액은 양수로 입력해주세요.")
        Integer startPrice,

        @NotNull(message = "마감 시간이 입력되지 않았습니다.")
        @Future(message = "마감 시간은 과거를 입력할 수 없습니다.")
        LocalDateTime closingTime,

        @NotNull(message = "하위 카테고리가 입력되지 않았습니다.")
        @Positive(message = "잘못된 카테고리 입니다.")
        Long subCategoryId,

        List<Long> thirdRegionIds
) {
}
