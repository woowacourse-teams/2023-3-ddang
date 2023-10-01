package com.ddang.ddang.chat.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class QuerydslMessageRepositoryImplFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaRegionRepository regionRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaMessageRepository messageRepository;

    protected User 판매자;
    protected User 구매자;
    protected Auction 경매;
    protected ChatRoom 채팅방;
    protected int 메시지_총_개수;

    @BeforeEach
    void setUp() {
        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(4.7d)
                  .oauthId("78923")
                  .build();
        구매자 = User.builder()
                  .name("구매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(4.7d)
                  .oauthId("12345")
                  .build();

        userRepository.save(판매자);
        userRepository.save(구매자);

        경매 = Auction.builder()
                    .title("title")
                    .build();

        auctionRepository.save(경매);

        채팅방 = new ChatRoom(경매, 구매자);

        chatRoomRepository.save(채팅방);

        메시지_총_개수 = 10;
        final List<Message> messages = new ArrayList<>();
        for (int count = 0; count < 메시지_총_개수; count++) {
            final Message message = Message.builder()
                                           .chatRoom(채팅방)
                                           .writer(판매자)
                                           .receiver(구매자)
                                           .contents("안녕하세요")
                                           .build();
            messages.add(message);
        }
        messageRepository.saveAll(messages);

        em.flush();
        em.clear();
    }
}
