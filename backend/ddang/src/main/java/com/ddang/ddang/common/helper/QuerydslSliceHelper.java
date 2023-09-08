package com.ddang.ddang.common.helper;

import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionCondition;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class QuerydslSliceHelper {

    private QuerydslSliceHelper() {
    }

    public static <T> Slice<T> toSlice(final List<T> contents, final ReadAuctionCondition readAuctionCondition) {
        final int size = readAuctionCondition.size();
        final boolean hasNext = isContentSizeGreaterThanPageSize(contents, size);
        final Pageable pageable = PageRequest.ofSize(size);

        if (hasNext) {
            return new SliceImpl<>(getSubListAfterLastContent(contents, size), pageable, hasNext);
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private static <T> boolean isContentSizeGreaterThanPageSize(final List<T> contents, final int size) {
        return contents.size() > size;
    }

    private static <T> List<T> getSubListAfterLastContent(final List<T> content, final int size) {
        return content.subList(0, size);
    }
}
