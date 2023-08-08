package com.ddang.ddang.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String profileImage;

    private double reliability;

    public User(final String name, final String profileImage, final double reliability) {
        this.name = name;
        this.profileImage = profileImage;
        this.reliability = reliability;
    }

    public User(final Long id, final String name, final String profileImage, final double reliability) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.reliability = reliability;
    }
}
