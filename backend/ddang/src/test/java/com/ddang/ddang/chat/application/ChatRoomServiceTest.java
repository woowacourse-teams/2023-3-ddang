package com.ddang.ddang.chat.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.application.exception.UserNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Test
    void 사용자가_참여한_모든_채팅방을_조회한다() {
        // given
        final Category main = new Category("메인");
        final Category sub = new Category("서브");
        main.addSubCategory(sub);
        categoryRepository.save(main);

        final User merry = new User("메리", "이미지", 5.0);
        final User encho = new User("엔초", "이미지", 5.0);
        final User jamie = new User("제이미", "이미지", 5.0);
        final User zeeto = new User("지토", "이미지", 5.0);
        userRepository.save(merry);
        userRepository.save(encho);
        userRepository.save(jamie);
        userRepository.save(zeeto);

        final Auction merryAuction = Auction.builder()
                                            .title("경매 1")
                                            .seller(merry)
                                            .subCategory(sub)
                                            .bidUnit(new BidUnit(1_000))
                                            .startPrice(new Price(10_000))
                                            .build();
        final Auction enchoAuction = Auction.builder()
                                            .title("경매 2")
                                            .seller(encho)
                                            .subCategory(sub)
                                            .bidUnit(new BidUnit(2_000))
                                            .startPrice(new Price(20_000))
                                            .build();
        final Auction jamieAuction = Auction.builder()
                                            .title("경매 3")
                                            .seller(jamie)
                                            .subCategory(sub)
                                            .bidUnit(new BidUnit(3_000))
                                            .startPrice(new Price(30_000))
                                            .build();

        auctionRepository.save(merryAuction);
        auctionRepository.save(enchoAuction);
        auctionRepository.save(jamieAuction);

        final ChatRoom merryZeeto = new ChatRoom(merryAuction, zeeto);
        final ChatRoom enchoZeeto = new ChatRoom(enchoAuction, zeeto);
        final ChatRoom jamieEncho = new ChatRoom(jamieAuction, encho);
        chatRoomRepository.save(merryZeeto);
        chatRoomRepository.save(enchoZeeto);
        chatRoomRepository.save(jamieEncho);

        // when
        final List<ReadParticipatingChatRoomDto> actual =
                chatRoomService.readAllByUserId(encho.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(enchoZeeto.getId());
            softAssertions.assertThat(actual.get(0).auctionDto().id()).isEqualTo(enchoZeeto.getAuction().getId());
            softAssertions.assertThat(actual.get(0).partnerDto().id()).isEqualTo(zeeto.getId());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(jamieEncho.getId());
            softAssertions.assertThat(actual.get(1).auctionDto().id()).isEqualTo(jamieEncho.getAuction().getId());
            softAssertions.assertThat(actual.get(1).partnerDto().id()).isEqualTo(jamie.getId());
        });
    }

    @Test
    void 사용자_정보를_찾을_수_없다면_예외가_발생한다() {
        // given
        final Long invalidUserId = -999L;

        // when & then
        assertThatThrownBy(() -> chatRoomService.readAllByUserId(invalidUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("사용자 정보를 찾을 수 없습니다.");
    }
}
