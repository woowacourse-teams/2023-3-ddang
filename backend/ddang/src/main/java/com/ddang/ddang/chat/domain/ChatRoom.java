package com.ddang.ddang.chat.domain;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.common.entity.BaseTimeEntity;
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
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString
public class ChatRoom extends BaseTimeEntity {

    public static final long CHAT_EXPIRATION_DAY = 10L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // 의도하는 것 : Auction이 삭제되어도 ChatRoom은 삭제되지 않음. ChatRoom 조회 시 Auction은 lazy 로딩
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", foreignKey = @ForeignKey(name = "fk_chat_room_seller"))
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", foreignKey = @ForeignKey(name = "fk_chat_room_buyer"))
    private User buyer;

    public ChatRoom(final Auction auction, final User seller, final User buyer) {
        this.auction = auction;
        this.seller = seller;
        this.buyer = buyer;
    }

    // TODO : 쪽지 비활성화 일수 리팩토링시 수정
    public boolean isChatAvailable() {
        final LocalDateTime maxChatTime = getCreatedTime().plusDays(CHAT_EXPIRATION_DAY);

        return LocalDateTime.now()
                            .isBefore(maxChatTime);
    }
}
