package com.ddang.ddang.image.application.util;

import com.ddang.ddang.image.domain.ProfileImage;

public final class ImageIdProcessor {

    private ImageIdProcessor() {
    }

    public static Long process(final ProfileImage profileImage) {
        if (profileImage == null) {
            return null;
        }

        return profileImage.getId();
    }
}
