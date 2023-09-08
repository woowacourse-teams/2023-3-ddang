package com.ddang.ddang.auction.configuration;

import com.ddang.ddang.auction.configuration.exception.InvalidAuctionConditionException;
import com.ddang.ddang.auction.configuration.util.AuctionSortConditionConsts;
import com.ddang.ddang.auction.configuration.util.SortParameter;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionCondition;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ReadAuctionConditionArgumentResolver implements HandlerMethodArgumentResolver {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final int DEFAULT_SIZE = 10;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(ReadAuctionCondition.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter ignoredParameter,
            final ModelAndViewContainer ignoredMavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory ignoredBinderFactory
    ) {
        final String sortParameter = processSortParameter(webRequest.getParameter("sort"));
        final Long lastAuctionId = processLastAuctionId(webRequest.getParameter("lastAuctionId"));
        final Integer lastAuctioneerCount = processLastAuctioneerCount(webRequest.getParameter("lastAuctioneerCount"));
        final LocalDateTime lastClosingTime = processLastClosingTime(webRequest.getParameter("lastClosingTime"));
        final Double lastReliability = processLastReliability(webRequest.getParameter("lastReliability"));
        final String title = webRequest.getParameter("title");
        final int size = processSizeParameter(webRequest.getParameter("size"));

        if (hasNoCondition(sortParameter, lastAuctionId)) {
            return ReadAuctionCondition.from(size);
        }

        if (isNotSortById(lastAuctionId, sortParameter) &&
                (isInvalidSortByAuctioneerCount(sortParameter, lastAuctioneerCount) ||
                        isInvalidSortByClosingTime(sortParameter, lastClosingTime) ||
                        isInvalidSortByReliability(sortParameter, lastReliability)))
        {
            throw new InvalidAuctionConditionException("유효하지 않은 경매 목록 조회 설정입니다.");
        }

        return new ReadAuctionCondition(
                sortParameter,
                lastAuctionId,
                lastAuctioneerCount,
                lastClosingTime,
                lastReliability,
                title,
                size
        );
    }

    private boolean hasNoCondition(final String sortParameter, final Long lastAuctionId) {
        return sortParameter == null && lastAuctionId == null;
    }

    private String processSortParameter(final String sortProperty) {
        if (sortProperty == null) {
            return AuctionSortConditionConsts.ID;
        }

        return SortParameter.findSortProperty(sortProperty);
    }

    private Long processLastAuctionId(final String lastAuctionId) {
        if (lastAuctionId == null) {
            return null;
        }

        return Long.valueOf(lastAuctionId);
    }

    private Integer processLastAuctioneerCount(final String lastAuctioneerCount) {
        if (lastAuctioneerCount == null) {
            return null;
        }

        return Integer.valueOf(lastAuctioneerCount);
    }

    private LocalDateTime processLastClosingTime(final String lastClosingTime) {
        if (lastClosingTime == null) {
            return null;
        }

        return LocalDateTime.parse(lastClosingTime, formatter);
    }

    private Double processLastReliability(final String lastReliability) {
        if (lastReliability == null) {
            return null;
        }

        return Double.valueOf(lastReliability);
    }

    private int processSizeParameter(final String sizeParameter) {
        if (sizeParameter == null) {
            return DEFAULT_SIZE;
        }

        return Integer.parseInt(sizeParameter);
    }

    private boolean isNotSortById(final Long lastAuctionId, final String sortParameter) {
        return lastAuctionId != null && !AuctionSortConditionConsts.ID.equals(sortParameter);
    }

    private boolean isInvalidSortByAuctioneerCount(final String sortParameter, final Integer lastAuctioneerCount) {
        return AuctionSortConditionConsts.AUCTIONEER_COUNT.equals(sortParameter) &&
                (lastAuctioneerCount == null || lastAuctioneerCount < 0);
    }

    private boolean isInvalidSortByClosingTime(final String sortParameter, final LocalDateTime lastClosingTime) {
        return AuctionSortConditionConsts.CLOSING_TINE.equals(sortParameter) && lastClosingTime == null;
    }

    private boolean isInvalidSortByReliability(final String sortParameter, final Double lastReliability) {
        return AuctionSortConditionConsts.RELIABILITY.equals(sortParameter) &&
                (lastReliability == null || lastReliability < 0.0d);
    }
}
