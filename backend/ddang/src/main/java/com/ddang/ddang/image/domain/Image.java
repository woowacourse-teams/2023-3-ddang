package com.ddang.ddang.image.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Image {

    private String uploadName;

    private String storeName;

    public Image(final String uploadName, final String storeName) {
        this.uploadName = uploadName;
        this.storeName = storeName;
    }
}
