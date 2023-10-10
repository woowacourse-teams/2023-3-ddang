package com.ddang.ddang.auction.configuration;

import com.ddang.ddang.auction.configuration.exception.InvalidSearchConditionException;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuctionSearchConditionArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int MINIMUM_TITLE_SEARCH_CONDITION_LENGTH = 2;
    private static final int MAXIMUM_TITLE_SEARCH_CONDITION_LENGTH = 30;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(ReadAuctionSearchCondition.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter ignoredParameter,
            final ModelAndViewContainer ignoredMavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory ignoredBinderFactory)
    {
        final String titleSearchCondition = webRequest.getParameter("title");

        if (titleSearchCondition == null) {
            return new ReadAuctionSearchCondition(null);
        }

        validateSearchCondition(titleSearchCondition);

        return new ReadAuctionSearchCondition(titleSearchCondition);
    }

    private void validateSearchCondition(final String titleSearchCondition) {
        final int length = titleSearchCondition.length();

        if (length < MINIMUM_TITLE_SEARCH_CONDITION_LENGTH || length > MAXIMUM_TITLE_SEARCH_CONDITION_LENGTH) {

            throw new InvalidSearchConditionException("경매 목록 제목 검색의 길이가 유효하지 않습니다.");
        }
    }
}
