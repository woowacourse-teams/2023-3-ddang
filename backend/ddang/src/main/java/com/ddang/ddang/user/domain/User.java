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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String profileImage;

    private double reliability;

    @Column(unique = true)
    private String oauthId;

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

    public User(final Long id, final String name, final String profileImage, final double reliability) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.reliability = reliability;
    }
}
