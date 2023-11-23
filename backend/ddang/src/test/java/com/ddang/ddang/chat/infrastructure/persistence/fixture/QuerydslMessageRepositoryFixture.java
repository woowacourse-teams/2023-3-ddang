package com.ddang.ddang.chat.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.MessageRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.QuerydslMessageRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class QuerydslMessageRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private JpaAuctionRepository jpaAuctionRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    private JpaMessageRepository jpaMessageRepository;

    protected User 판매자;
    protected User 구매자;
    protected Auction 경매;
    protected ChatRoom 채팅방;
    protected int 메시지_총_개수;
    protected List<Message> 저장된_메시지들;
    protected Message 세_번째_메시지;
    protected Message 네_번째_메시지;
    protected Message 다섯_번째_메시지;
    protected Message 여섯_번째_메시지;
    protected Message 일곱_번째_메시지;
    protected Message 여덟_번째_메시지;
    protected Message 아홉_번째_메시지;
    protected Message 열_번째_메시지;

    @BeforeEach
    void setUp() {
        final AuctionRepository auctionRepository = new AuctionRepositoryImpl(
                jpaAuctionRepository,
                new QuerydslAuctionRepository(queryFactory)
        );
        final MessageRepository messageRepository = new MessageRepositoryImpl(
                jpaMessageRepository,
                new QuerydslMessageRepository(queryFactory)
        );

        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
                  .oauthId("78923")
                  .build();
        구매자 = User.builder()
                  .name("구매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
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
        저장된_메시지들 = new ArrayList<>();
        for (int count = 0; count < 메시지_총_개수; count++) {
            final Message 메시지 = Message.builder()
                                       .chatRoom(채팅방)
                                       .writer(판매자)
                                       .receiver(구매자)
                                       .content("안녕하세요")
                                       .build();
            저장된_메시지들.add(메시지);
            messageRepository.save(메시지);
        }

        세_번째_메시지 = 저장된_메시지들.get(2);
        네_번째_메시지 = 저장된_메시지들.get(3);
        다섯_번째_메시지 = 저장된_메시지들.get(4);
        여섯_번째_메시지 = 저장된_메시지들.get(5);
        일곱_번째_메시지 = 저장된_메시지들.get(6);
        여덟_번째_메시지 = 저장된_메시지들.get(7);
        아홉_번째_메시지 = 저장된_메시지들.get(8);
        열_번째_메시지 = 저장된_메시지들.get(9);

        em.flush();
        em.clear();
    }
}
