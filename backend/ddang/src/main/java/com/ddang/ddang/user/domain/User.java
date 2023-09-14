package com.ddang.ddang.user.domain;

import com.ddang.ddang.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Table(name = "users")
public class User extends BaseTimeEntity {

    private static final boolean DELETED_STATUS = true;
    private static final String UNKOWN_NAME = "알 수 없음";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String profileImage;

    private double reliability;

    private String oauthId;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Builder
    private User(
            final String name,
            final String profileImage,
            final double reliability,
            final String oauthId
    ) {
        this.name = name;
        this.profileImage = profileImage;
        this.reliability = reliability;
        this.oauthId = oauthId;
    }

    public void withdrawal() {
        this.deleted = DELETED_STATUS;
    }

    // TODO: 2023/09/15 [고민] getName() 메서드를 통해 가져오게 된다면 헷갈릴까요?
    // TODO: 2023/09/15 [고민] 출력을 위한 로직이 여기 있다면 어색할까요? 사실 귀찮아서 여기 두긴 했습니다.
    public String getName() {
        if (isDeleted()) {
            return UNKOWN_NAME;
        }

        return name;
    }
}
