package com.ddang.ddang.device.domain;

import com.ddang.ddang.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private User user;

    @Column(name = "device_token", unique = true)
    private String deviceToken;

    public DeviceToken(final User user, final String deviceToken) {
        this.user = user;
        this.deviceToken = deviceToken;
    }

    public boolean isDifferentToken(final String targetDeviceToken) {
        return !this.deviceToken.equals(targetDeviceToken);
    }

    public void updateDeviceToken(final String newDeviceToken) {
        this.deviceToken = newDeviceToken;
    }
}
