package com.ddang.ddang.chat.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class JpaMessageRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    protected User 채팅참여_판매자;
    protected User 채팅참여_구매자;
    protected Auction 경매;
    protected Message 메시지;
    protected Long 유효하지_않은_메시지_아이디 = -999L;

    @BeforeEach
    void setUp() {
        // given
        채팅참여_판매자 = User.builder()
                       .name("판매자")
                       .profileImage(new ProfileImage("upload.png", "store.png"))
                       .reliability(new Reliability(4.7f))
                       .oauthId("12345")
                       .build();
        채팅참여_구매자 = User.builder()
                       .name("구매자")
                       .profileImage(new ProfileImage("upload.png", "store.png"))
                       .reliability(new Reliability(4.7f))
                       .oauthId("12346")
                       .build();

        userRepository.save(채팅참여_판매자);
        userRepository.save(채팅참여_구매자);

        경매 = Auction.builder()
                    .title("경매")
                    .build();

        auctionRepository.save(경매);

        final ChatRoom 채팅방 = new ChatRoom(경매, 채팅참여_구매자);

        chatRoomRepository.save(채팅방);

        메시지 = Message.builder()
                     .chatRoom(채팅방)
                     .writer(채팅참여_판매자)
                     .receiver(채팅참여_구매자)
                     .contents("안녕하세요")
                     .build();

        em.flush();
        em.clear();
    }
}
