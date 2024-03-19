package com.ddang.ddang.chat.handler.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class ChatWebSocketHandleTextMessageProviderTestFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    protected ChatRoom 채팅방;
    protected User 발신자;
    protected User 수신자;

    protected Map<String, Object> 발신자_세션_attribute_정보;
    protected Map<String, Object> 수신자_세션_attribute_정보;
    protected Map<String, String> 메시지_전송_데이터;

    @BeforeEach
    void setUpFixture() {
        final Category 전자기기 = new Category("전자기기");
        final Category 전자기기_하위_노트북 = new Category("노트북");
        전자기기.addSubCategory(전자기기_하위_노트북);
        categoryRepository.save(전자기기);

        final Auction 경매 = Auction.builder()
                                  .title("경매")
                                  .description("description")
                                  .bidUnit(new BidUnit(1_000))
                                  .startPrice(new Price(10_000))
                                  .closingTime(LocalDateTime.now().plusDays(3L))
                                  .build();
        auctionRepository.save(경매);

        발신자 = User.builder()
                  .name("발신자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();
        수신자 = User.builder()
                  .name("수신자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
                  .oauthId("12346")
                  .build();
        userRepository.save(발신자);
        userRepository.save(수신자);

        채팅방 = new ChatRoom(경매, 발신자);

        chatRoomRepository.save(채팅방);

        발신자_세션_attribute_정보 = new HashMap<>(Map.of("userId", 발신자.getId(), "baseUrl", "/images"));
        수신자_세션_attribute_정보 = new HashMap<>(Map.of("userId", 수신자.getId(), "baseUrl", "/images"));
        메시지_전송_데이터 = Map.of(
                "chatRoomId", String.valueOf(채팅방.getId()),
                "receiverId", String.valueOf(수신자.getId()),
                "contents", "메시지 내용"
        );
    }
}
