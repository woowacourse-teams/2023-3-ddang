package com.ddang.ddang.chat.application.fixture;

import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadUserInChatRoomDto;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.domain.ChatRoomAndImageDto;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    private JpaMessageRepository messageRepository;

    protected User 판매자;
    protected User 구매자;
    protected User 엔초;
    protected User 제이미;
    protected User 지토;
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
    protected ReadParticipatingChatRoomDto 엔초가_조회한_엔초_지토_채팅방_정보_조회_결과;
    protected ReadChatRoomDto 엔초_지토_채팅방_정보_및_참여_가능;
    protected ReadChatRoomDto 엔초_지토_채팅방_정보_및_참여_불가능;
    protected ReadChatRoomDto 채팅방은_아직_없지만_참여_가능;
    protected ReadChatRoomDto 채팅방_없고_참여_불가능;
    protected ReadChatRoomWithLastMessageDto 엔초_채팅_목록의_제이미_엔초_채팅방_정보;
    protected ReadChatRoomWithLastMessageDto 엔초_채팅_목록의_엔초_지토_채팅방_정보;

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
                  .oauthId("12346")
                  .build();
        제이미 = User.builder()
                   .name("제이미")
                   .profileImage(프로필_이미지)
                   .reliability(new Reliability(4.7d))
                   .oauthId("12347")
                   .build();
        지토 = User.builder()
                  .name("지토")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12348")
                  .build();
        경매에_참여한_적_없는_사용자 = User.builder()
                                    .name("외부인")
                                    .profileImage(프로필_이미지)
                                    .reliability(new Reliability(4.7d))
                                    .oauthId("12349")
                                    .build();
        userRepository.saveAll(List.of(판매자, 구매자, 엔초, 제이미, 지토, 경매에_참여한_적_없는_사용자));

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
        채팅방이_없는_경매.addAuctionImages(List.of(경매_대표_이미지, 대표_이미지가_아닌_경매_이미지));
        판매자_엔초_구매자_지토_경매.addAuctionImages(List.of(엔초의_경매_대표_이미지, 엔초의_대표_이미지가_아닌_경매_이미지));
        판매자_제이미_구매자_엔초_경매.addAuctionImages(List.of(제이미의_경매_대표_이미지, 제이미의_대표_이미지가_아닌_경매_이미지));
        auctionRepository.saveAll(
                List.of(채팅방이_없는_경매, 종료되지_않은_경매, 낙찰자가_없는_경매, 판매자_엔초_구매자_지토_경매, 판매자_제이미_구매자_엔초_경매)
        );

        final Bid 채팅방_없는_경매_입찰 = new Bid(채팅방이_없는_경매, 구매자, new BidPrice(15_000));
        final Bid 지토가_엔초_경매에_입찰 = new Bid(판매자_엔초_구매자_지토_경매, 지토, new BidPrice(15_000));
        final Bid 엔초가_제이미_경매에_입찰 = new Bid(판매자_제이미_구매자_엔초_경매, 엔초, new BidPrice(15_000));
        bidRepository.saveAll(List.of(채팅방_없는_경매_입찰, 지토가_엔초_경매에_입찰, 엔초가_제이미_경매에_입찰));
        채팅방이_없는_경매.updateLastBid(채팅방_없는_경매_입찰);
        판매자_엔초_구매자_지토_경매.updateLastBid(지토가_엔초_경매에_입찰);
        판매자_제이미_구매자_엔초_경매.updateLastBid(엔초가_제이미_경매에_입찰);

        엔초_지토_채팅방 = new ChatRoom(판매자_엔초_구매자_지토_경매, 지토);
        제이미_엔초_채팅방 = new ChatRoom(판매자_제이미_구매자_엔초_경매, 엔초);
        chatRoomRepository.saveAll(List.of(엔초_지토_채팅방, 제이미_엔초_채팅방));

        엔초가_지토에게_1시에_보낸_쪽지 = Message.builder()
                                          .chatRoom(엔초_지토_채팅방)
                                          .contents("엔초가 지토에게 1시애 보낸 쪽지")
                                          .writer(엔초)
                                          .receiver(지토)
                                          .build();
        제이미가_엔초에게_2시에_보낸_쪽지 = Message.builder()
                                           .chatRoom(제이미_엔초_채팅방)
                                           .contents("제이미가 엔초에게 2시애 보낸 쪽지")
                                           .writer(제이미)
                                           .receiver(엔초)
                                           .build();
        messageRepository.saveAll(List.of(엔초가_지토에게_1시에_보낸_쪽지, 제이미가_엔초에게_2시에_보낸_쪽지));

        final ChatRoomAndImageDto 엔초_지토_채팅방_정보 = new ChatRoomAndImageDto(엔초_지토_채팅방, 엔초의_경매_대표_이미지);
        엔초_회원_정보 = new AuthenticationUserInfo(엔초.getId());
        판매자_회원_정보 = new AuthenticationUserInfo(판매자.getId());
        경매에_참여한_적_없는_사용자_정보 = new AuthenticationUserInfo(경매에_참여한_적_없는_사용자.getId());
        존재하지_않는_사용자_정보 = new AuthenticationUserInfo(존재하지_않는_사용자_아이디);
        채팅방_생성을_위한_DTO = new CreateChatRoomDto(채팅방이_없는_경매.getId());
        경매_정보가_없어서_채팅방을_생성할_수_없는_DTO = new CreateChatRoomDto(존재하지_않는_경매_아이디);
        경매가_진행중이라서_채팅방을_생성할_수_없는_DTO = new CreateChatRoomDto(종료되지_않은_경매.getId());
        낙찰자가_없어서_채팅방을_생성할_수_없는_DTO = new CreateChatRoomDto(낙찰자가_없는_경매.getId());
        엔초_지토_채팅방_생성을_위한_DTO = new CreateChatRoomDto(판매자_엔초_구매자_지토_경매.getId());
        엔초가_조회한_엔초_지토_채팅방_정보_조회_결과 = ReadParticipatingChatRoomDto.of(엔초, 엔초_지토_채팅방_정보);
        엔초_지토_채팅방_정보_및_참여_가능 = new ReadChatRoomDto(엔초_지토_채팅방.getId(), true);
        엔초_지토_채팅방_정보_및_참여_불가능 = new ReadChatRoomDto(엔초_지토_채팅방.getId(), false);
        채팅방은_아직_없지만_참여_가능 = new ReadChatRoomDto(null, true);
        채팅방_없고_참여_불가능 = new ReadChatRoomDto(null, false);
        엔초_채팅_목록의_제이미_엔초_채팅방_정보 = new ReadChatRoomWithLastMessageDto(
                제이미_엔초_채팅방.getId(),
                ReadAuctionInChatRoomDto.of(판매자_제이미_구매자_엔초_경매, 제이미의_경매_대표_이미지),
                ReadUserInChatRoomDto.from(제이미),
                ReadLastMessageDto.from(제이미가_엔초에게_2시에_보낸_쪽지),
                true
        );
        엔초_채팅_목록의_엔초_지토_채팅방_정보 = new ReadChatRoomWithLastMessageDto(
                엔초_지토_채팅방.getId(),
                ReadAuctionInChatRoomDto.of(판매자_엔초_구매자_지토_경매, 엔초의_경매_대표_이미지),
                ReadUserInChatRoomDto.from(지토),
                ReadLastMessageDto.from(엔초가_지토에게_1시에_보낸_쪽지),
                true
        );
    }
}
