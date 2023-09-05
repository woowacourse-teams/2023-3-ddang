package com.ddang.ddang.auction.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionWithChatRoomIdDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionServiceTest {

    @MockBean
    StoreImageProcessor imageProcessor;

    @Autowired
    AuctionService auctionService;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaRegionRepository regionRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaBidRepository bidRepository;

    @Test
    void 경매를_등록한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();

        userRepository.save(seller);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        // when
        final CreateInfoAuctionDto actual = auctionService.create(createAuctionDto);

        // then
        assertThat(actual.id()).isPositive();
    }

    @Test
    void 지정한_아이디에_대한_회원이_없는_경우_경매를_등록하면_예외가_발생한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1}
        );

        final Long invalidSellerId = -999L;

        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                invalidSellerId
        );

        // when & then
        assertThatThrownBy(() -> auctionService.create(createAuctionDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("지정한 판매자를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_지역이_없을때_경매를_등록하면_예외가_발생한다() {
        // given
        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("789321")
                                .build();

        userRepository.save(seller);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(3L),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        // when & then
        assertThatThrownBy(() -> auctionService.create(createAuctionDto))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 세 번째 지역이 없거나 세 번째 지역이 아닙니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_지역이_세_번째_지역이_아닐_떄_경매를_등록하면_예외가_발생한다() {
        // given
        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("54321")
                                .build();

        userRepository.save(seller);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(secondRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        // when & then
        assertThatThrownBy(() -> auctionService.create(createAuctionDto))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 세 번째 지역이 없거나 세 번째 지역이 아닙니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_카테고리가_없을때_경매를_등록하면_예외가_발생한다() {
        // given
        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();

        userRepository.save(seller);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                1L,
                List.of(auctionImage),
                1L
        );

        // when & then
        assertThatThrownBy(() -> auctionService.create(createAuctionDto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_카테고리가_하위_카테고리가_아닐_떄_경매를_등록하면_예외가_발생한다() {
        // given
        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();

        userRepository.save(seller);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                main.getId(),
                List.of(auctionImage),
                seller.getId()
        );

        // when & then
        assertThatThrownBy(() -> auctionService.create(createAuctionDto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다.");
    }

    @Test
    void 채팅방이_존재하고_채팅_자격이_있는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디와_채팅_가능을_반환한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();

        userRepository.save(seller);
        userRepository.save(buyer);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        final Auction auction = createAuctionDto.toEntity(seller, sub);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        // when
        final ReadAuctionWithChatRoomIdDto actual =
                auctionService.readByAuctionId(auction.getId(), new AuthenticationUserInfo(seller.getId()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.auctionDto().id()).isPositive();
            softAssertions.assertThat(actual.auctionDto().title()).isEqualTo(auction.getTitle());
            softAssertions.assertThat(actual.auctionDto().description()).isEqualTo(auction.getDescription());
            softAssertions.assertThat(actual.auctionDto().bidUnit()).isEqualTo(auction.getBidUnit().getValue());
            softAssertions.assertThat(actual.auctionDto().startPrice()).isEqualTo(auction.getStartPrice().getValue());
            softAssertions.assertThat(actual.auctionDto().lastBidPrice()).isNull();
            softAssertions.assertThat(actual.auctionDto().deleted()).isFalse();
            softAssertions.assertThat(actual.auctionDto().closingTime()).isEqualTo(auction.getClosingTime());
            softAssertions.assertThat(actual.auctionDto().auctioneerCount()).isEqualTo(0);
            softAssertions.assertThat(actual.chatRoomDto().id()).isEqualTo(chatRoom.getId());
            softAssertions.assertThat(actual.chatRoomDto().isChatParticipant()).isTrue();
        });
    }

    @Test
    void 채팅방이_존재하고_채팅_자격이_없는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디와_채팅_불가를_반환한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User stranger = User.builder()
                                  .name("회원3")
                                  .profileImage("profile.png")
                                  .reliability(4.7d)
                                  .oauthId("12347")
                                  .build();

        userRepository.save(seller);
        userRepository.save(buyer);
        userRepository.save(stranger);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        final Auction auction = createAuctionDto.toEntity(seller, sub);
        auctionRepository.save(auction);

        final ChatRoom chatRoom = new ChatRoom(auction, buyer);
        chatRoomRepository.save(chatRoom);

        // when
        final ReadAuctionWithChatRoomIdDto actual =
                auctionService.readByAuctionId(auction.getId(), new AuthenticationUserInfo(stranger.getId()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.auctionDto().id()).isPositive();
            softAssertions.assertThat(actual.auctionDto().title()).isEqualTo(auction.getTitle());
            softAssertions.assertThat(actual.auctionDto().description()).isEqualTo(auction.getDescription());
            softAssertions.assertThat(actual.auctionDto().bidUnit()).isEqualTo(auction.getBidUnit().getValue());
            softAssertions.assertThat(actual.auctionDto().startPrice()).isEqualTo(auction.getStartPrice().getValue());
            softAssertions.assertThat(actual.auctionDto().lastBidPrice()).isNull();
            softAssertions.assertThat(actual.auctionDto().deleted()).isFalse();
            softAssertions.assertThat(actual.auctionDto().closingTime()).isEqualTo(auction.getClosingTime());
            softAssertions.assertThat(actual.auctionDto().auctioneerCount()).isEqualTo(0);
            softAssertions.assertThat(actual.chatRoomDto().id()).isEqualTo(chatRoom.getId());
            softAssertions.assertThat(actual.chatRoomDto().isChatParticipant()).isFalse();
        });
    }

    @Test
    void 경매가_종료되지_않은_상태에서_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디_null과_채팅_불가를_반환한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();

        userRepository.save(seller);
        userRepository.save(buyer);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        final Auction auction = createAuctionDto.toEntity(seller, sub);
        auctionRepository.save(auction);

        // when
        final ReadAuctionWithChatRoomIdDto actual =
                auctionService.readByAuctionId(auction.getId(), new AuthenticationUserInfo(seller.getId()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.auctionDto().id()).isPositive();
            softAssertions.assertThat(actual.auctionDto().title()).isEqualTo(auction.getTitle());
            softAssertions.assertThat(actual.auctionDto().description()).isEqualTo(auction.getDescription());
            softAssertions.assertThat(actual.auctionDto().bidUnit()).isEqualTo(auction.getBidUnit().getValue());
            softAssertions.assertThat(actual.auctionDto().startPrice()).isEqualTo(auction.getStartPrice().getValue());
            softAssertions.assertThat(actual.auctionDto().lastBidPrice()).isNull();
            softAssertions.assertThat(actual.auctionDto().deleted()).isFalse();
            softAssertions.assertThat(actual.auctionDto().closingTime()).isEqualTo(auction.getClosingTime());
            softAssertions.assertThat(actual.auctionDto().auctioneerCount()).isEqualTo(0);
            softAssertions.assertThat(actual.chatRoomDto().id()).isEqualTo(null);
            softAssertions.assertThat(actual.chatRoomDto().isChatParticipant()).isFalse();
        });
    }

    @Test
    void 경매가_종료되었지만_채팅방이_없는_상태에서_채팅_자격이_있는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디_null과_채팅_가능을_반환한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();

        userRepository.save(seller);
        userRepository.save(buyer);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        final Auction auction = createAuctionDto.toEntity(seller, sub);
        auctionRepository.save(auction);

        // when
        final ReadAuctionWithChatRoomIdDto actual =
                auctionService.readByAuctionId(auction.getId(), new AuthenticationUserInfo(seller.getId()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.auctionDto().id()).isPositive();
            softAssertions.assertThat(actual.auctionDto().title()).isEqualTo(auction.getTitle());
            softAssertions.assertThat(actual.auctionDto().description()).isEqualTo(auction.getDescription());
            softAssertions.assertThat(actual.auctionDto().bidUnit()).isEqualTo(auction.getBidUnit().getValue());
            softAssertions.assertThat(actual.auctionDto().startPrice()).isEqualTo(auction.getStartPrice().getValue());
            softAssertions.assertThat(actual.auctionDto().lastBidPrice()).isNull();
            softAssertions.assertThat(actual.auctionDto().deleted()).isFalse();
            softAssertions.assertThat(actual.auctionDto().closingTime()).isEqualTo(auction.getClosingTime());
            softAssertions.assertThat(actual.auctionDto().auctioneerCount()).isEqualTo(0);
            softAssertions.assertThat(actual.chatRoomDto().id()).isEqualTo(null);
            softAssertions.assertThat(actual.chatRoomDto().isChatParticipant()).isTrue();
        });
    }

    @Test
    void 채팅방이_없고_채팅_자격이_없는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디_null과_채팅_불가를_반환한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User stranger = User.builder()
                                  .name("회원3")
                                  .profileImage("profile.png")
                                  .reliability(4.7d)
                                  .oauthId("12347")
                                  .build();

        userRepository.save(seller);
        userRepository.save(buyer);
        userRepository.save(stranger);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        final Auction auction = createAuctionDto.toEntity(seller, sub);
        auctionRepository.save(auction);

        // when
        final ReadAuctionWithChatRoomIdDto actual =
                auctionService.readByAuctionId(auction.getId(), new AuthenticationUserInfo(stranger.getId()));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.auctionDto().id()).isPositive();
            softAssertions.assertThat(actual.auctionDto().title()).isEqualTo(auction.getTitle());
            softAssertions.assertThat(actual.auctionDto().description()).isEqualTo(auction.getDescription());
            softAssertions.assertThat(actual.auctionDto().bidUnit()).isEqualTo(auction.getBidUnit().getValue());
            softAssertions.assertThat(actual.auctionDto().startPrice()).isEqualTo(auction.getStartPrice().getValue());
            softAssertions.assertThat(actual.auctionDto().lastBidPrice()).isNull();
            softAssertions.assertThat(actual.auctionDto().deleted()).isFalse();
            softAssertions.assertThat(actual.auctionDto().closingTime()).isEqualTo(auction.getClosingTime());
            softAssertions.assertThat(actual.auctionDto().auctioneerCount()).isEqualTo(0);
            softAssertions.assertThat(actual.chatRoomDto().id()).isEqualTo(null);
            softAssertions.assertThat(actual.chatRoomDto().isChatParticipant()).isFalse();
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매가_없는_경매를_조회시_예외가_발생한다() {
        // given
        final Long invalidAuctionId = -999L;
        final AuthenticationUserInfo userInfo = new AuthenticationUserInfo(1L);

        // when & then
        assertThatThrownBy(() -> auctionService.readByAuctionId(invalidAuctionId, userInfo))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("지정한 아이디에 대한 경매를 찾을 수 없습니다.");
    }

    @Test
    void 요청을_한_회원의_정보를_찾을_수_없으면_예외가_발생한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User buyer = User.builder()
                               .name("회원2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();

        userRepository.save(seller);
        userRepository.save(buyer);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        final Auction auction = createAuctionDto.toEntity(seller, sub);
        auctionRepository.save(auction);

        final Long auctionId = auction.getId();
        final Long invalidUserId = -999L;
        AuthenticationUserInfo userInfo = new AuthenticationUserInfo(invalidUserId);

        // when & then
        assertThatThrownBy(() -> auctionService.readByAuctionId(auctionId, userInfo))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
    }

    @Test
    void 첫번째_페이지의_경매_목록을_조회한다() {
        // given
        final StoreImageDto storeImageDto = new StoreImageDto("upload.png", "store.png");

        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(storeImageDto));

        final Region firstRegion = new Region("first");
        final Region secondRegion = new Region("second");
        final Region thirdRegion = new Region("third");

        firstRegion.addSecondRegion(secondRegion);
        secondRegion.addThirdRegion(thirdRegion);

        regionRepository.save(firstRegion);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();

        userRepository.save(seller);

        final MockMultipartFile auctionImage = new MockMultipartFile(
                "image.png",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1});
        final CreateAuctionDto createAuctionDto1 = new CreateAuctionDto(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );
        final CreateAuctionDto createAuctionDto2 = new CreateAuctionDto(
                "경매 상품 2",
                "이것은 경매 상품 2 입니다.",
                1_000,
                1_000,
                LocalDateTime.now(),
                List.of(thirdRegion.getId()),
                sub.getId(),
                List.of(auctionImage),
                1L
        );

        auctionService.create(createAuctionDto1);
        auctionService.create(createAuctionDto2);

        // when
        final ReadAuctionsDto actual = auctionService.readAllByLastAuctionId(null, PageRequest.of(1, 1));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            final List<ReadAuctionDto> actualReadAuctionDtos = actual.readAuctionDtos();
            softAssertions.assertThat(actualReadAuctionDtos).hasSize(1);
            softAssertions.assertThat(actualReadAuctionDtos.get(0).title()).isEqualTo(createAuctionDto2.title());
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_삭제한다() {
        // given
        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();

        userRepository.save(seller);

        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .seller(seller)
                                       .build();

        auctionRepository.save(auction);

        // when
        auctionService.deleteByAuctionId(auction.getId(), seller.getId());

        // then
        final Optional<Auction> actual = auctionRepository.findById(auction.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().isDeleted()).isTrue();
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매가_없는_경매를_삭제시_예외가_발생한다() {
        // given
        final Long invalidAuctionId = -999L;

        // when & then
        assertThatThrownBy(() -> auctionService.deleteByAuctionId(invalidAuctionId, 1L))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("지정한 아이디에 대한 경매를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_회원이_없는_경우_삭제시_예외가_발생한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();

        userRepository.save(seller);
        auctionRepository.save(auction);
        final Long invalidSellerId = -999L;
        final Long persistAuctionId = auction.getId();

        // when & then
        assertThatThrownBy(() -> auctionService.deleteByAuctionId(persistAuctionId, invalidSellerId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_회원과_판매자가_일치하지_않는_경우_삭제시_예외가_발생한다() {
        // given
        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        userRepository.save(seller);
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .seller(seller)
                                       .build();
        final User user = User.builder()
                              .name("회원2")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12346")
                              .build();

        userRepository.save(user);
        auctionRepository.save(auction);

        final Long persistAuctionId = auction.getId();
        final Long invalidSellerId = user.getId();

        // when & then
        assertThatThrownBy(() -> auctionService.deleteByAuctionId(persistAuctionId, invalidSellerId))
                .isInstanceOf(UserForbiddenException.class)
                .hasMessage("권한이 없습니다.");
    }
}
