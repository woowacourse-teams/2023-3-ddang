package com.ddang.ddang.image.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "image"})
public class ProfileImage {

    public static final String DEFAULT_PROFILE_IMAGE_STORE_NAME = "default_profile_image.png";
    // TODO: 10/13/23 앞으로 id가 아닌 store name으로 진행하기로 했는데, 임시로 해둡니다. 추후 삭제해주시면 감사하겠습니다.
    public static final String DEFAULT_PROFILE_IMAGE_ID = "1";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Image image;

    public ProfileImage(final String uploadName, final String storeName) {
        this.image = new Image(uploadName, storeName);
    }

    public String getStoreName() {
        return image.getStoreName();
    }
}
