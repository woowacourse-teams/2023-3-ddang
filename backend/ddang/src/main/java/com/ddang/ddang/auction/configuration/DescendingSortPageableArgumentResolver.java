package com.ddang.ddang.auction.configuration;

import com.ddang.ddang.auction.configuration.util.SortParameter;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class DescendingSortPageableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Pageable.class) &&
                parameter.hasParameterAnnotation(DescendingSort.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter ignoredParameter,
            final ModelAndViewContainer ignoredMavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory ignoredBinderFactory
    ) {
        final int size = processSizeParameter(webRequest.getParameter("size"));
        final int page = processPageParameter(webRequest.getParameter("page"));
        final Sort sort = processSortParameter(webRequest.getParameter("sort"));

        return PageRequest.of(page, size, sort);
    }

    private int processSizeParameter(final String sizeParameter) {
        if (sizeParameter == null) {
            return DEFAULT_SIZE;
        }

        return Integer.parseInt(sizeParameter);
    }

    private int processPageParameter(final String pageParameter) {
        if (pageParameter == null) {
            return DEFAULT_PAGE;
        }

        return Integer.parseInt(pageParameter) - 1;
    }

    private Sort processSortParameter(final String sortParameter) {
        return Sort.by(Direction.DESC, SortParameter.findSortProperty(sortParameter));
    }
}
