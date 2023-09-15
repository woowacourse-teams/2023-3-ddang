package com.ddang.ddang.user.domain;

import com.ddang.ddang.common.entity.BaseTimeEntity;
import com.ddang.ddang.image.domain.ProfileImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@ToString(of = {"id", "name", "reliability", "oauthId", "deleted"})
@Table(name = "users")
public class User extends BaseTimeEntity {

    private static final boolean DELETED_STATUS = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "profile_image_id", foreignKey = @ForeignKey(name = "fk_user_profile_image"))
    private ProfileImage profileImage;

    private double reliability;

    @Column(unique = true)
    private String oauthId;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Builder
    private User(
            final String name,
            final ProfileImage profileImage,
            final double reliability,
            final String oauthId
    ) {
        this.name = name;
        this.profileImage = profileImage;
        this.reliability = reliability;
        this.oauthId = oauthId;
    }

    public void update(final String name, final ProfileImage profileProfileImage) {
        this.name = name;
        this.profileImage = profileProfileImage;
    }

    public void withdrawal() {
        this.deleted = DELETED_STATUS;
    }
}
