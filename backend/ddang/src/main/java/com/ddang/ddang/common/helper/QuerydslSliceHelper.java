package com.ddang.ddang.common.helper;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class QuerydslSliceHelper {

    private QuerydslSliceHelper() {
    }

    public static <T> Slice<T> toSlice(List<T> contents, int size) {
        final boolean hasNext = isContentSizeGreaterThanPageSize(contents, size);
        final Pageable pageable = PageRequest.ofSize(size);

        if (hasNext) {
            return new SliceImpl<>(subListLastContent(contents, size), pageable, hasNext);
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private static <T> boolean isContentSizeGreaterThanPageSize(List<T> content, int size) {
        return content.size() > size;
    }

    private static <T> List<T> subListLastContent(List<T> content, int size) {
        return content.subList(0, size);
    }
}
