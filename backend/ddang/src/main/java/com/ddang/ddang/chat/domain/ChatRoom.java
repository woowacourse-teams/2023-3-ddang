package com.ddang.ddang.chat.domain;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.common.entity.BaseCreateTimeEntity;
import com.ddang.ddang.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class ChatRoom extends BaseCreateTimeEntity {

    public static final long CHAT_EXPIRATION_DAY = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chat_room_auction"))
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chat_room_buyer"))
    private User buyer;

    public ChatRoom(final Auction auction, final User buyer) {
        this.auction = auction;
        this.buyer = buyer;
    }

    // TODO : 쪽지 비활성화 일수 리팩토링시 수정
    public boolean isChatAvailableTime(final LocalDateTime targetTime) {
        final LocalDateTime maxChatTime = getCreatedTime().plusDays(CHAT_EXPIRATION_DAY);

        return targetTime.isBefore(maxChatTime);
    }

    public User calculateChatPartnerOf(final User user) {
        if (isSeller(user)) {
            return buyer;
        }

        return auction.getSeller();
    }

    public boolean isParticipant(final User user) {
        if (isSeller(user)) {
            return true;
        }

        return isBuyer(user);
    }

    private boolean isSeller(final User user) {
        return auction.getSeller().equals(user);
    }

    private boolean isBuyer(final User user) {
        return buyer.equals(user);
    }
}
