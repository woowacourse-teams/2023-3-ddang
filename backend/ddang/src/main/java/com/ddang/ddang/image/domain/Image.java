package com.ddang.ddang.image.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: 2023/09/08 [고민] 현재는 프로필 이미지만 담당하지만, 추후 Q&A, 채팅에 이미지가 사용될 수 있을 것 같아 Image로만 이름을 지었는데 의견 부탁드립니다.
// TODO: 2023/09/08 [고민] 아니면 AuctionIamge가 Image를 상속받는 형태는 어떻게 생각하시나요?
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "uploadName", "storeName"})
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadName;

    private String storeName;

    public Image(final String uploadName, final String storeName) {
        this.uploadName = uploadName;
        this.storeName = storeName;
    }
}
