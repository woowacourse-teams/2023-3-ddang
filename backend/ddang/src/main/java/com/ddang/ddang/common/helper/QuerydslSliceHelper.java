package com.ddang.ddang.common.helper;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class QuerydslSliceHelper {

    private QuerydslSliceHelper() {
    }

    public static <T> Slice<T> toSlice(final List<T> contents, final Pageable pageable) {
        final int size = pageable.getPageSize();
        final boolean hasNext = isContentSizeGreaterThanPageSize(contents, size);

        if (hasNext) {
            return new SliceImpl<>(subListLastContent(contents, size), pageable, hasNext);
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private static <T> boolean isContentSizeGreaterThanPageSize(final List<T> contents, final int size) {
        return contents.size() > size;
    }

    private static <T> List<T> subListLastContent(List<T> content, int size) {
        return content.subList(0, size);
    }
}
