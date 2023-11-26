package com.ddang.ddang.chat.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.domain.repository.BidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.dto.request.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.response.ReadChatRoomDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto.ReadChatRoomAuctionInfoDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto.ReadLastMessageDto;
import com.ddang.ddang.chat.application.dto.response.ReadMultipleChatRoomDto.ReadPartnerInfoDto;
import com.ddang.ddang.chat.application.dto.response.ReadSingleChatRoomDto;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomServiceFixture {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    protected User 판매자;
    protected User 구매자;
    protected User 엔초;
    protected User 제이미;
    protected User 지토;
    protected User 채팅방을_생성하는_메리;
    protected User 메리_경매_낙찰자_지토;
    protected User 경매에_참여한_적_없는_사용자;
    protected Auction 채팅방이_없는_경매;
    protected Auction 판매자_엔초_구매자_지토_경매;
    protected ChatRoom 엔초_지토_채팅방;
    private ChatRoom 제이미_엔초_채팅방;
    private Message 엔초가_지토에게_1시에_보낸_쪽지;
    private Message 제이미가_엔초에게_2시에_보낸_쪽지;
    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected Long 존재하지_않는_경매_아이디 = -999L;
    protected Long 존재하지_않는_채팅방_아이디 = -999L;
    protected AuthenticationUserInfo 엔초_회원_정보;
    protected AuthenticationUserInfo 판매자_회원_정보;
    protected AuthenticationUserInfo 경매에_참여한_적_없는_사용자_정보;
    protected AuthenticationUserInfo 존재하지_않는_사용자_정보;
    protected CreateChatRoomDto 채팅방_생성을_위한_DTO;
    protected CreateChatRoomDto 경매_정보가_없어서_채팅방을_생성할_수_없는_DTO;
    protected CreateChatRoomDto 경매가_진행중이라서_채팅방을_생성할_수_없는_DTO;
    protected CreateChatRoomDto 낙찰자가_없어서_채팅방을_생성할_수_없는_DTO;
    protected CreateChatRoomDto 엔초_지토_채팅방_생성을_위한_DTO;
    protected CreateChatRoomDto 메리가_생성하려는_채팅방;
    protected ReadSingleChatRoomDto 엔초가_조회한_엔초_지토_채팅방_정보_조회_결과;
    protected ReadChatRoomDto 엔초_지토_채팅방_정보_및_참여_가능;
    protected ReadChatRoomDto 엔초_지토_채팅방_정보_및_참여_불가능;
    protected ReadChatRoomDto 채팅방은_아직_없지만_참여_가능;
    protected ReadChatRoomDto 채팅방_없고_참여_불가능;
    protected ReadMultipleChatRoomDto 엔초_채팅_목록의_제이미_엔초_채팅방_정보;
    protected ReadMultipleChatRoomDto 엔초_채팅_목록의_엔초_지토_채팅방_정보;

    @BeforeEach
    void setUp() {
        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        categoryRepository.save(전자기기_카테고리);

        final ProfileImage 프로필_이미지 = new ProfileImage("upload.png", "store.png");
        final AuctionImage 경매_대표_이미지 = new AuctionImage("경매_대표_이미지.png", "경매_대표_이미지.png");
        final AuctionImage 대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("대표 이미지가_아닌_경매_이미지.png", "대표 이미지가_아닌_경매_이미지.png");
        final AuctionImage 엔초의_경매_대표_이미지 = new AuctionImage("엔초의_경매_대표_이미지.png", "엔초의_경매_대표_이미지.png");
        final AuctionImage 엔초의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("엔초의_대표 이미지가_아닌_경매_이미지.png", "엔초의_대표 이미지가_아닌_경매_이미지.png");
        final AuctionImage 제이미의_경매_대표_이미지 = new AuctionImage("제이미의_경매_대표_이미지.png", "제이미의_경매_대표_이미지.png");
        final AuctionImage 제이미의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("제이미의_대표 이미지가_아닌_경매_이미지.png", "제이미의_대표 이미지가_아닌_경매_이미지.png");

        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();
        구매자 = User.builder()
                  .name("구매자")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12346")
                  .build();
        엔초 = User.builder()
                 .name("엔초")
                 .profileImage(프로필_이미지)
                 .reliability(new Reliability(4.7d))
                 .oauthId("12347")
                 .build();
        제이미 = User.builder()
                  .name("제이미")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12348")
                  .build();
        지토 = User.builder()
                 .name("지토")
                 .profileImage(프로필_이미지)
                 .reliability(new Reliability(4.7d))
                 .oauthId("12349")
                 .build();
        경매에_참여한_적_없는_사용자 = User.builder()
                               .name("외부인")
                               .profileImage(프로필_이미지)
                               .reliability(new Reliability(4.7d))
                               .oauthId("12340")
                               .build();
        채팅방을_생성하는_메리 = User.builder()
                           .name("채팅방을_생성하는_메리")
                           .profileImage(프로필_이미지)
                           .reliability(new Reliability(4.7d))
                           .oauthId("09876")
                           .build();
        메리_경매_낙찰자_지토 = User.builder()
                           .name("메리_경매_낙찰자_지토")
                           .profileImage(프로필_이미지)
                           .reliability(new Reliability(4.7d))
                           .oauthId("10293")
                           .build();
        userRepository.save(판매자);
        userRepository.save(구매자);
        userRepository.save(엔초);
        userRepository.save(제이미);
        userRepository.save(지토);
        userRepository.save(경매에_참여한_적_없는_사용자);
        userRepository.save(채팅방을_생성하는_메리);
        userRepository.save(메리_경매_낙찰자_지토);

        채팅방이_없는_경매 = Auction.builder()
                            .seller(판매자)
                            .title("맥북")
                            .description("맥북 팔아요")
                            .subCategory(전자기기_서브_노트북_카테고리)
                            .startPrice(new Price(10_000))
                            .bidUnit(new BidUnit(1_000))
                            .closingTime(LocalDateTime.now())
                            .build();
        final Auction 종료되지_않은_경매 = Auction.builder()
                                          .seller(판매자)
                                          .title("맥북")
                                          .description("맥북 팔아요")
                                          .subCategory(전자기기_서브_노트북_카테고리)
                                          .startPrice(new Price(10_000))
                                          .bidUnit(new BidUnit(1_000))
                                          .closingTime(LocalDateTime.now().plusDays(10L))
                                          .build();
        final Auction 낙찰자가_없는_경매 = Auction.builder()
                                          .seller(판매자)
                                          .title("맥북")
                                          .description("맥북 팔아요")
                                          .subCategory(전자기기_서브_노트북_카테고리)
                                          .startPrice(new Price(10_000))
                                          .bidUnit(new BidUnit(1_000))
                                          .closingTime(LocalDateTime.now())
                                          .build();
        판매자_엔초_구매자_지토_경매 = Auction.builder()
                                  .seller(엔초)
                                  .title("엔초 맥북")
                                  .description("엔초 맥북 팔아요")
                                  .subCategory(전자기기_서브_노트북_카테고리)
                                  .startPrice(new Price(10_000))
                                  .bidUnit(new BidUnit(1_000))
                                  .closingTime(LocalDateTime.now())
                                  .build();
        final Auction 판매자_제이미_구매자_엔초_경매 = Auction.builder()
                                                 .seller(제이미)
                                                 .title("제이미 맥북")
                                                 .description("제이미 맥북 팔아요")
                                                 .subCategory(전자기기_서브_노트북_카테고리)
                                                 .startPrice(new Price(10_000))
                                                 .bidUnit(new BidUnit(1_000))
                                                 .closingTime(LocalDateTime.now())
                                                 .build();
        final Auction 판매자_메리_구매자_지토_경매 = Auction.builder()
                                                .seller(채팅방을_생성하는_메리)
                                                .title("메리 맥북")
                                                .description("메리 맥북 팔아요")
                                                .subCategory(전자기기_서브_노트북_카테고리)
                                                .startPrice(new Price(10_000))
                                                .bidUnit(new BidUnit(1_000))
                                                .closingTime(LocalDateTime.now())
                                                .build();
        채팅방이_없는_경매.addAuctionImages(List.of(경매_대표_이미지, 대표_이미지가_아닌_경매_이미지));
        판매자_엔초_구매자_지토_경매.addAuctionImages(List.of(엔초의_경매_대표_이미지, 엔초의_대표_이미지가_아닌_경매_이미지));
        판매자_제이미_구매자_엔초_경매.addAuctionImages(List.of(제이미의_경매_대표_이미지, 제이미의_대표_이미지가_아닌_경매_이미지));
        판매자_메리_구매자_지토_경매.addAuctionImages(List.of(경매_대표_이미지, 대표_이미지가_아닌_경매_이미지));

        auctionRepository.save(채팅방이_없는_경매);
        auctionRepository.save(종료되지_않은_경매);
        auctionRepository.save(낙찰자가_없는_경매);
        auctionRepository.save(판매자_엔초_구매자_지토_경매);
        auctionRepository.save(판매자_제이미_구매자_엔초_경매);
        auctionRepository.save(판매자_메리_구매자_지토_경매);

        final Bid 채팅방_없는_경매_입찰 = new Bid(채팅방이_없는_경매, 구매자, new BidPrice(15_000));
        final Bid 지토가_엔초_경매에_입찰 = new Bid(판매자_엔초_구매자_지토_경매, 지토, new BidPrice(15_000));
        final Bid 엔초가_제이미_경매에_입찰 = new Bid(판매자_제이미_구매자_엔초_경매, 엔초, new BidPrice(15_000));
        final Bid 지토가_메리_경매에_입찰 = new Bid(판매자_메리_구매자_지토_경매, 메리_경매_낙찰자_지토, new BidPrice(15_000));
        bidRepository.save(채팅방_없는_경매_입찰);
        bidRepository.save(지토가_엔초_경매에_입찰);
        bidRepository.save(엔초가_제이미_경매에_입찰);
        bidRepository.save(지토가_메리_경매에_입찰);
        채팅방이_없는_경매.updateLastBid(채팅방_없는_경매_입찰);
        판매자_엔초_구매자_지토_경매.updateLastBid(지토가_엔초_경매에_입찰);
        판매자_제이미_구매자_엔초_경매.updateLastBid(엔초가_제이미_경매에_입찰);
        판매자_메리_구매자_지토_경매.updateLastBid(지토가_메리_경매에_입찰);

        엔초_지토_채팅방 = new ChatRoom(판매자_엔초_구매자_지토_경매, 지토);
        제이미_엔초_채팅방 = new ChatRoom(판매자_제이미_구매자_엔초_경매, 엔초);

        chatRoomRepository.save(엔초_지토_채팅방);
        chatRoomRepository.save(제이미_엔초_채팅방);

        엔초가_지토에게_1시에_보낸_쪽지 = Message.builder()
                                    .chatRoom(엔초_지토_채팅방)
                                    .content("엔초가 지토에게 1시애 보낸 쪽지")
                                    .writer(엔초)
                                    .receiver(지토)
                                    .build();
        제이미가_엔초에게_2시에_보낸_쪽지 = Message.builder()
                                     .chatRoom(제이미_엔초_채팅방)
                                     .content("제이미가 엔초에게 2시애 보낸 쪽지")
                                     .writer(제이미)
                                     .receiver(엔초)
                                     .build();
        messageRepository.save(엔초가_지토에게_1시에_보낸_쪽지);
        messageRepository.save(제이미가_엔초에게_2시에_보낸_쪽지);

        엔초_회원_정보 = new AuthenticationUserInfo(엔초.getId());
        판매자_회원_정보 = new AuthenticationUserInfo(판매자.getId());
        경매에_참여한_적_없는_사용자_정보 = new AuthenticationUserInfo(경매에_참여한_적_없는_사용자.getId());
        존재하지_않는_사용자_정보 = new AuthenticationUserInfo(존재하지_않는_사용자_아이디);
        채팅방_생성을_위한_DTO = new CreateChatRoomDto(채팅방이_없는_경매.getId());
        메리가_생성하려는_채팅방 = new CreateChatRoomDto(판매자_메리_구매자_지토_경매.getId());
        경매_정보가_없어서_채팅방을_생성할_수_없는_DTO = new CreateChatRoomDto(존재하지_않는_경매_아이디);
        경매가_진행중이라서_채팅방을_생성할_수_없는_DTO = new CreateChatRoomDto(종료되지_않은_경매.getId());
        낙찰자가_없어서_채팅방을_생성할_수_없는_DTO = new CreateChatRoomDto(낙찰자가_없는_경매.getId());
        엔초_지토_채팅방_생성을_위한_DTO = new CreateChatRoomDto(판매자_엔초_구매자_지토_경매.getId());
        엔초가_조회한_엔초_지토_채팅방_정보_조회_결과 = ReadSingleChatRoomDto.of(엔초, 엔초_지토_채팅방);
        엔초_지토_채팅방_정보_및_참여_가능 = new ReadChatRoomDto(엔초_지토_채팅방.getId(), true);
        엔초_지토_채팅방_정보_및_참여_불가능 = new ReadChatRoomDto(엔초_지토_채팅방.getId(), false);
        채팅방은_아직_없지만_참여_가능 = new ReadChatRoomDto(null, true);
        채팅방_없고_참여_불가능 = new ReadChatRoomDto(null, false);
        엔초_채팅_목록의_제이미_엔초_채팅방_정보 = new ReadMultipleChatRoomDto(
                제이미_엔초_채팅방.getId(),
                new ReadChatRoomAuctionInfoDto(판매자_제이미_구매자_엔초_경매.getId(), 판매자_제이미_구매자_엔초_경매.getTitle(), 판매자_제이미_구매자_엔초_경매.getLastBid().getPrice().getValue(), 판매자_제이미_구매자_엔초_경매.getThumbnailImageStoreName()),
                new ReadPartnerInfoDto(제이미.getId(), 제이미.getName(), 제이미.getProfileImageStoreName(), 제이미.getReliability().getValue(), 제이미.isDeleted()),
                new ReadLastMessageDto(제이미가_엔초에게_2시에_보낸_쪽지.getCreatedTime(), 제이미가_엔초에게_2시에_보낸_쪽지.getContent()),
                1L,
                true
        );
        엔초_채팅_목록의_엔초_지토_채팅방_정보 = new ReadMultipleChatRoomDto(
                엔초_지토_채팅방.getId(),
                new ReadChatRoomAuctionInfoDto(판매자_엔초_구매자_지토_경매.getId(), 판매자_엔초_구매자_지토_경매.getTitle(), 판매자_엔초_구매자_지토_경매.getLastBid().getPrice().getValue(), 판매자_엔초_구매자_지토_경매.getThumbnailImageStoreName()),
                new ReadPartnerInfoDto(지토.getId(), 지토.getName(), 지토.getProfileImageStoreName(), 지토.getReliability().getValue(), 지토.isDeleted()),
                new ReadLastMessageDto(엔초가_지토에게_1시에_보낸_쪽지.getCreatedTime(), 엔초가_지토에게_1시에_보낸_쪽지.getContent()),
                1L,
                true
        );
    }
}
