package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.UserNotAccessibleException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaMessageRepository messageRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaBidRepository bidRepository;

    @Test
    void 채팅방을_생성한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("회원2").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder().title("경매").description("설명").seller(seller)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L)).subCategory(sub).build();
        auctionRepository.save(auction);

        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);

        auction.updateLastBid(bid);

        final Long auctionId = auction.getId();
        final Long userId = buyer.getId();
        final CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(auctionId);

        // when
        final Long actual = chatRoomService.create(userId, createChatRoomDto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 채팅방_생성시_요청한_사용자_정보를_찾을_수_없다면_예외가_발생한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("회원2").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder().title("경매").description("설명").seller(seller)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L)).subCategory(sub).build();
        auctionRepository.save(auction);

        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);

        auction.updateLastBid(bid);

        final Long auctionId = auction.getId();
        final Long invalidUserId = -999L;
        final CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(auctionId);

        // when & then
        assertThatThrownBy(() -> chatRoomService.create(invalidUserId, createChatRoomDto)).isInstanceOf(UserNotFoundException.class)
                                                                                          .hasMessage("사용자 정보를 찾을 수 없습니다.");
    }

    @Test
    void 채팅방_생성시_관련된_경매_정보를_찾을_수_없다면_예외가_발생한다() {
        // given
        final User user = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                              .build();
        userRepository.save(user);

        final Long userId = user.getId();
        final Long invalidAuctionId = -999L;
        final CreateChatRoomDto invalidDto = new CreateChatRoomDto(invalidAuctionId);


        // when & then
        assertThatThrownBy(() -> chatRoomService.create(userId, invalidDto)).isInstanceOf(AuctionNotFoundException.class)
                                                                            .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 경매가_종료되지_않은_상태에서_채팅방을_생성하면_예외가_발생한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("회원2").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder().title("경매").description("설명").seller(seller)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().plusDays(3L)).subCategory(sub).build();
        auctionRepository.save(auction);

        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);

        auction.updateLastBid(bid);

        final Long auctionId = auction.getId();
        final Long userId = buyer.getId();
        final CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(auctionId);

        // when & then
        assertThatThrownBy(() -> chatRoomService.create(userId, createChatRoomDto)).isInstanceOf(InvalidAuctionToChatException.class)
                                                                                   .hasMessage("경매가 아직 종료되지 않았습니다.");
    }

    @Test
    void 경매가_삭제된_상태에서_채팅방을_생성하면_예외가_발생한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("회원2").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder().title("경매").description("설명").seller(seller)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L)).subCategory(sub).build();
        auctionRepository.save(auction);

        auction.delete();

        final Long auctionId = auction.getId();
        final Long userId = buyer.getId();
        final CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(auctionId);

        // when & then
        assertThatThrownBy(() -> chatRoomService.create(userId, createChatRoomDto)).isInstanceOf(InvalidAuctionToChatException.class)
                                                                                   .hasMessage("삭제된 경매입니다.");
    }

    @Test
    void 낙찰자가_없는데_채팅방을_생성하면_예외가_발생한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        userRepository.save(seller);

        final Auction auction = Auction.builder().title("경매").description("설명").seller(seller)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L)).subCategory(sub).build();
        auctionRepository.save(auction);

        final Long auctionId = auction.getId();
        final Long userId = seller.getId();
        final CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(auctionId);

        // when & then
        assertThatThrownBy(() -> chatRoomService.create(userId, createChatRoomDto)).isInstanceOf(WinnerNotFoundException.class)
                                                                                   .hasMessage("낙찰자가 존재하지 않습니다");
    }

    @Test
    void 채팅방_생성을_요청한_사용자가_경매의_판매자_또는_최종_낙찰자가_아니라면_예외가_발생한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("회원2").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        final User stranger = User.builder().name("회원3").profileImage("profile.png").reliability(4.7d).oauthId("12347")
                                  .build();
        userRepository.save(seller);
        userRepository.save(buyer);
        userRepository.save(stranger);

        final Auction auction = Auction.builder().title("경매").description("설명").seller(seller)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L)).subCategory(sub).build();
        auctionRepository.save(auction);

        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);

        auction.updateLastBid(bid);

        final Long auctionId = auction.getId();
        final Long strangeUserId = stranger.getId();
        final CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(auctionId);

        // when & then
        assertThatThrownBy(() -> chatRoomService.create(strangeUserId, createChatRoomDto)).isInstanceOf(UserNotAccessibleException.class)
                                                                                          .hasMessage("경매의 판매자 또는 최종 낙찰자만 채팅이 가능합니다.");
    }

    @Test
    void 해당_경매에_대한_채팅이_이미_존재할_경우_존재하는_채팅방의_아이디를_반환한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder().name("회원1").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("회원2").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Auction auction = Auction.builder().title("경매").description("설명").seller(seller)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L)).subCategory(sub).build();
        auctionRepository.save(auction);

        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);

        auction.updateLastBid(bid);

        final Long auctionId = auction.getId();
        final Long userId = buyer.getId();
        final CreateChatRoomDto createChatRoomDto = new CreateChatRoomDto(auctionId);

        final ChatRoom persistChatRoom = new ChatRoom(auction, auction.findWinner(LocalDateTime.now()).get());
        chatRoomRepository.save(persistChatRoom);

        final Long expect = persistChatRoom.getId();

        // when
        final Long actual = chatRoomService.create(userId, createChatRoomDto);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 사용자가_참여한_모든_채팅방을_마지막에_전송된_메시지와_함께_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User merry = User.builder().name("메리").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                               .build();
        final User encho = User.builder().name("엔초").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        final User jamie = User.builder().name("제이미").profileImage("profile.png").reliability(4.7d).oauthId("12347")
                               .build();
        final User zeeto = User.builder().name("지토").profileImage("profile.png").reliability(4.7d).oauthId("12348")
                               .build();
        userRepository.save(merry);
        userRepository.save(encho);
        userRepository.save(jamie);
        userRepository.save(zeeto);

        final Auction merryAuction = Auction.builder().title("경매 1").seller(merry).subCategory(sub)
                                            .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000)).build();
        final Auction enchoAuction = Auction.builder().title("경매 2").seller(encho).subCategory(sub)
                                            .bidUnit(new BidUnit(2_000)).startPrice(new Price(20_000)).build();
        final Auction jamieAuction = Auction.builder().title("경매 3").seller(jamie).subCategory(sub)
                                            .bidUnit(new BidUnit(3_000)).startPrice(new Price(30_000)).build();

        auctionRepository.save(merryAuction);
        auctionRepository.save(enchoAuction);
        auctionRepository.save(jamieAuction);

        final ChatRoom merryZeeto = new ChatRoom(merryAuction, zeeto);
        final ChatRoom enchoZeeto = new ChatRoom(enchoAuction, zeeto);
        final ChatRoom jamieEncho = new ChatRoom(jamieAuction, encho);
        chatRoomRepository.save(merryZeeto);
        chatRoomRepository.save(enchoZeeto);
        chatRoomRepository.save(jamieEncho);

        final Message message1 = Message.builder()
                                        .chatRoom(jamieEncho)
                                        .contents("jamieEncho message 1")
                                        .writer(jamie)
                                        .receiver(encho)
                                        .build();
        messageRepository.save(message1);
        final Message lastMessage1 = Message.builder()
                                            .chatRoom(jamieEncho)
                                            .contents("jamieEncho message 2")
                                            .writer(jamie)
                                            .receiver(encho)
                                            .build();
        messageRepository.save(lastMessage1);
        final Message lastMessage2 = Message.builder()
                                            .chatRoom(enchoZeeto)
                                            .writer(encho)
                                            .receiver(zeeto)
                                            .contents("enchoZeeto message 1")
                                            .build();
        messageRepository.save(lastMessage2);

        // when
        final List<ReadChatRoomWithLastMessageDto> actual = chatRoomService.readAllByUserId(encho.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(enchoZeeto.getId());
            softAssertions.assertThat(actual.get(1).auctionDto().id()).isEqualTo(enchoZeeto.getAuction().getId());
            softAssertions.assertThat(actual.get(1).partnerDto().id()).isEqualTo(zeeto.getId());
            softAssertions.assertThat(actual.get(1).lastMessageDto().id()).isEqualTo(lastMessage2.getId());
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(jamieEncho.getId());
            softAssertions.assertThat(actual.get(0).auctionDto().id()).isEqualTo(jamieEncho.getAuction().getId());
            softAssertions.assertThat(actual.get(0).partnerDto().id()).isEqualTo(jamie.getId());
            softAssertions.assertThat(actual.get(0).lastMessageDto().id()).isEqualTo(lastMessage1.getId());
        });
    }

    @Test
    void 사용자가_참여한_채팅방_목록_조회시_사용자_정보를_찾을_수_없다면_예외가_발생한다() {
        // given
        final Long invalidUserId = -999L;

        // when & then
        assertThatThrownBy(() -> chatRoomService.readAllByUserId(invalidUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("사용자 정보를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방을_조회한다() {
        // given
        final User seller = User.builder().name("판매자").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("구매자").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        userRepository.save(seller);
        userRepository.save(buyer);

        final Category main = new Category("main");
        final Category sub = new Category("sub");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final Auction auction = Auction.builder().title("경매").seller(seller).subCategory(sub)
                                       .bidUnit(new BidUnit(1_000)).startPrice(new Price(10_000))
                                       .closingTime(LocalDateTime.now().minusDays(6)).build();
        auctionRepository.save(auction);
        final Bid bid = new Bid(auction, buyer, new BidPrice(15_000));
        bidRepository.save(bid);
        auction.updateLastBid(bid);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        final ReadParticipatingChatRoomDto expect = ReadParticipatingChatRoomDto.of(seller, chatRoom, LocalDateTime.now());

        // when
        final ReadParticipatingChatRoomDto actual = chatRoomService.readByChatRoomId(chatRoom.getId(), seller.getId());

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_조회를_요청한_사용자의_정보를_찾을_수_없다면_예외가_발생한다() {
        // given
        final User user = User.builder().name("구매자").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                              .build();
        userRepository.save(user);

        final Auction auction = Auction.builder().title("경매").build();
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, user);
        chatRoomRepository.save(chatRoom);

        final Long chatRoomId = chatRoom.getId();
        final Long invalidUserId = -999L;

        // when & then
        assertThatThrownBy(() -> chatRoomService.readByChatRoomId(chatRoomId, invalidUserId)).isInstanceOf(UserNotFoundException.class)
                                                                                             .hasMessageContaining("사용자 정보를 찾을 수 없습니다.");

    }

    @Test
    void 지정한_아이디에_해당하는_채팅방을_찾을_수_없다면_예외가_발생한다() {
        // given
        final User user = User.builder().name("구매자").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                              .build();
        userRepository.save(user);

        final Long invalidChatRoomId = -999L;
        final Long userId = user.getId();

        // when & then
        assertThatThrownBy(() -> chatRoomService.readByChatRoomId(invalidChatRoomId, userId)).isInstanceOf(ChatRoomNotFoundException.class)
                                                                                             .hasMessageContaining("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");

    }

    @Test
    void 지정한_아이디에_해당하는_채팅방_조회시_주어진_사용자가_채팅의_참여자가_아니라면_예외가_발생한다() {
        // given
        final User seller = User.builder().name("판매자").profileImage("profile.png").reliability(4.7d).oauthId("12345")
                                .build();
        final User buyer = User.builder().name("구매자").profileImage("profile.png").reliability(4.7d).oauthId("12346")
                               .build();
        final User stranger = User.builder().name("일반인").profileImage("profile.png").reliability(4.7d).oauthId("12347")
                                  .build();
        userRepository.save(seller);
        userRepository.save(buyer);
        userRepository.save(stranger);

        final Auction auction = Auction.builder().title("경매").seller(seller).build();
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        final Long chatRoomId = chatRoom.getId();
        final Long nonAuthorizedUserId = stranger.getId();

        // when & then
        assertThatThrownBy(() -> chatRoomService.readByChatRoomId(chatRoomId, nonAuthorizedUserId)).isInstanceOf(UserNotAccessibleException.class)
                                                                                                   .hasMessageContaining("해당 채팅방에 접근할 권한이 없습니다.");
    }
}
