package com.ddang.ddang.user.domain;

import com.ddang.ddang.common.entity.BaseTimeEntity;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "name", "reliability", "oauthId", "deleted"})
@Table(name = "users")
public class User extends BaseTimeEntity {

    private static final boolean DELETED_STATUS = true;
    private static final String UNKOWN_NAME = "알 수 없음";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "profile_image_id", foreignKey = @ForeignKey(name = "fk_user_profile_image"), nullable = false)
    private ProfileImage profileImage;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reliability"))
    private Reliability reliability;

    private String oauthId;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Builder
    private User(
            @NonNull final String name,
            @NonNull final ProfileImage profileImage,
            @NonNull final Reliability reliability,
            @NonNull final String oauthId
    ) {
        this.name = name;
        this.profileImage = profileImage;
        this.reliability = reliability;
        this.oauthId = oauthId;
    }

    public void updateName(final String name) {
        this.name = name;
    }

    public void updateProfileImage(final ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public void withdrawal() {
        this.deleted = DELETED_STATUS;
    }

    public void updateReliability(final List<Review> reviews) {
        reliability.updateReliability(reviews);
    }
}
