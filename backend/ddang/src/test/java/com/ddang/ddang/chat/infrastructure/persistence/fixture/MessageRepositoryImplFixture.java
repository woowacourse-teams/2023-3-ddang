package com.ddang.ddang.chat.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.ChatRoomRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.MessageRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.QuerydslMessageRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImplFixture {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private AuctionRepository auctionRepository;
    private ChatRoomRepository chatRoomRepository;

    protected User 판매자_겸_발신자;
    protected User 구매자_겸_수신자;
    protected Auction 경매;
    protected Auction 메시지가_5개인_경매;
    protected ChatRoom 채팅방;
    protected ChatRoom 메시지가_5개인_채팅방;
    protected Message 저장할_메시지;
    protected Message 저장된_메시지;
    protected Message 세_번째_메시지;
    protected Message 네_번째_메시지;
    protected Message 다섯_번째_메시지;
    protected long 두_번째_메시지_아이디;

    protected String 메시지_내용 = "메시지 내용";
    protected long 존재하지_않는_메시지_아이디 = -999L;

    @BeforeEach
    void fixtureSetUp(
            @Autowired final JPAQueryFactory queryFactory,
            @Autowired final JpaMessageRepository jpaMessageRepository,
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaAuctionRepository jpaAuctionRepository,
            @Autowired final JpaChatRoomRepository jpaChatRoomRepository
    ) {
        messageRepository = new MessageRepositoryImpl(jpaMessageRepository, new QuerydslMessageRepository(queryFactory));
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        auctionRepository = new AuctionRepositoryImpl(jpaAuctionRepository, new QuerydslAuctionRepository((queryFactory)));
        chatRoomRepository = new ChatRoomRepositoryImpl(jpaChatRoomRepository);

        판매자_겸_발신자 = User.builder()
                        .name("판매자")
                        .profileImage(new ProfileImage("upload.png", "store.png"))
                        .reliability(new Reliability(4.7d))
                        .oauthId("78923")
                        .build();
        구매자_겸_수신자 = User.builder()
                        .name("구매자")
                        .profileImage(new ProfileImage("upload.png", "store.png"))
                        .reliability(new Reliability(4.7d))
                        .oauthId("12345")
                        .build();
        userRepository.save(판매자_겸_발신자);
        userRepository.save(구매자_겸_수신자);

        경매 = Auction.builder()
                    .title("title")
                    .build();
        메시지가_5개인_경매 = Auction.builder()
                             .title("메시지가_5개인_경매")
                             .build();
        auctionRepository.save(경매);
        auctionRepository.save(메시지가_5개인_경매);

        채팅방 = new ChatRoom(경매, 구매자_겸_수신자);
        메시지가_5개인_채팅방 = new ChatRoom(메시지가_5개인_경매, 구매자_겸_수신자);
        chatRoomRepository.save(채팅방);
        chatRoomRepository.save(메시지가_5개인_채팅방);

        저장할_메시지 = Message.builder()
                         .chatRoom(채팅방)
                         .writer(판매자_겸_발신자)
                         .receiver(구매자_겸_수신자)
                         .contents(메시지_내용)
                         .build();
        저장된_메시지 = Message.builder()
                         .chatRoom(채팅방)
                         .writer(판매자_겸_발신자)
                         .receiver(구매자_겸_수신자)
                         .contents("레포지토리에 저장된 메시지")
                         .build();
        messageRepository.save(저장된_메시지);

        int 메시지_총_개수 = 5;
        List<Message> 저장된_메시지들 = new ArrayList<>();
        for (int count = 0; count < 메시지_총_개수; count++) {
            final Message 메시지 = Message.builder()
                                       .chatRoom(메시지가_5개인_채팅방)
                                       .writer(판매자_겸_발신자)
                                       .receiver(구매자_겸_수신자)
                                       .contents("안녕하세요")
                                       .build();
            저장된_메시지들.add(메시지);
            messageRepository.save(메시지);
        }

        두_번째_메시지_아이디 = 저장된_메시지들.get(1).getId();
        세_번째_메시지 = 저장된_메시지들.get(2);
        네_번째_메시지 = 저장된_메시지들.get(3);
        다섯_번째_메시지 = 저장된_메시지들.get(4);
    }
}
