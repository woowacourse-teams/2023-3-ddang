package com.ddang.ddang.auction.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.auction.application.fixture.AuctionServiceFixture;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.domain.StoreImageProcessor;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionServiceTest extends AuctionServiceFixture {

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
        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(경매_이미지_엔티티));

        // when
        final CreateInfoAuctionDto actual = auctionService.create(유효한_경매_생성_dto);

        // then
        assertThat(actual.id()).isPositive();
    }

    @Test
    void 지정한_아이디에_대한_판매자가_없는_경우_경매를_등록하면_예외가_발생한다() {
        // given
        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(경매_이미지_엔티티));

        // when & then
        assertThatThrownBy(() -> auctionService.create(존재하지_않는_판매자의_경매_생성_dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("지정한 판매자를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_지역이_없을때_경매를_등록하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.create(존재하지_않는_지역의_경매_생성_dto))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 세 번째 지역이 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_지역이_세_번째_지역이_아닐_떄_경매를_등록하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.create(두_번째_지역의_경매_생성_dto))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("지정한 세 번째 지역이 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_카테고리가_없을때_경매를_등록하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.create(존재하지_않는_카테고리의_경매_생성_dto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_카테고리가_서브_카테고리가_아닐_떄_경매를_등록하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.create(메인_카테고리의_경매_생성_dto))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("지정한 하위 카테고리가 없거나 하위 카테고리가 아닙니다.");
    }

    @Test
    void 채팅방이_존재하고_채팅_자격이_있는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디와_채팅_가능을_반환한다() {
        // given
        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(경매_이미지_엔티티));

        // when
        final ReadAuctionDto actual = auctionService.readByAuctionId(채팅방이_있는_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.id()).isPositive();
            softAssertions.assertThat(actual.title()).isEqualTo(채팅방이_있는_경매.getTitle());
            softAssertions.assertThat(actual.description()).isEqualTo(채팅방이_있는_경매.getDescription());
            softAssertions.assertThat(actual.bidUnit()).isEqualTo(채팅방이_있는_경매.getBidUnit().getValue());
            softAssertions.assertThat(actual.startPrice()).isEqualTo(채팅방이_있는_경매.getStartPrice().getValue());
            softAssertions.assertThat(actual.lastBidPrice()).isNull();
            softAssertions.assertThat(actual.deleted()).isFalse();
            softAssertions.assertThat(actual.closingTime()).isEqualTo(채팅방이_있는_경매.getClosingTime());
            softAssertions.assertThat(actual.auctioneerCount()).isEqualTo(0);
        });
    }

    @Test
    void 채팅방이_존재하고_채팅_자격이_없는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디와_채팅_불가를_반환한다() {
        // when
        final ReadAuctionDto actual = auctionService.readByAuctionId(채팅방이_있는_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.id()).isPositive();
            softAssertions.assertThat(actual.title()).isEqualTo(채팅방이_있는_경매.getTitle());
            softAssertions.assertThat(actual.description()).isEqualTo(채팅방이_있는_경매.getDescription());
            softAssertions.assertThat(actual.bidUnit()).isEqualTo(채팅방이_있는_경매.getBidUnit().getValue());
            softAssertions.assertThat(actual.startPrice()).isEqualTo(채팅방이_있는_경매.getStartPrice().getValue());
            softAssertions.assertThat(actual.lastBidPrice()).isNull();
            softAssertions.assertThat(actual.deleted()).isFalse();
            softAssertions.assertThat(actual.closingTime()).isEqualTo(채팅방이_있는_경매.getClosingTime());
            softAssertions.assertThat(actual.auctioneerCount()).isEqualTo(0);
        });
    }

    @Test
    void 경매가_종료되지_않은_상태에서_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디_null과_채팅_불가를_반환한다() {
        // when
        final ReadAuctionDto actual = auctionService.readByAuctionId(채팅방이_있는_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.id()).isPositive();
            softAssertions.assertThat(actual.title()).isEqualTo(채팅방이_있는_경매.getTitle());
            softAssertions.assertThat(actual.description()).isEqualTo(채팅방이_있는_경매.getDescription());
            softAssertions.assertThat(actual.bidUnit()).isEqualTo(채팅방이_있는_경매.getBidUnit().getValue());
            softAssertions.assertThat(actual.startPrice()).isEqualTo(채팅방이_있는_경매.getStartPrice().getValue());
            softAssertions.assertThat(actual.lastBidPrice()).isNull();
            softAssertions.assertThat(actual.deleted()).isFalse();
            softAssertions.assertThat(actual.closingTime()).isEqualTo(채팅방이_있는_경매.getClosingTime());
            softAssertions.assertThat(actual.auctioneerCount()).isEqualTo(0);
        });
    }

    @Test
    void 경매가_종료되었지만_채팅방이_없는_상태에서_채팅_자격이_있는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디_null과_채팅_가능을_반환한다() {
        // given
        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(경매_이미지_엔티티));

        // when
        final ReadAuctionDto actual = auctionService.readByAuctionId(채팅방이_있는_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.id()).isPositive();
            softAssertions.assertThat(actual.title()).isEqualTo(채팅방이_있는_경매.getTitle());
            softAssertions.assertThat(actual.description()).isEqualTo(채팅방이_있는_경매.getDescription());
            softAssertions.assertThat(actual.bidUnit()).isEqualTo(채팅방이_있는_경매.getBidUnit().getValue());
            softAssertions.assertThat(actual.startPrice()).isEqualTo(채팅방이_있는_경매.getStartPrice().getValue());
            softAssertions.assertThat(actual.lastBidPrice()).isNull();
            softAssertions.assertThat(actual.deleted()).isFalse();
            softAssertions.assertThat(actual.closingTime()).isEqualTo(채팅방이_있는_경매.getClosingTime());
            softAssertions.assertThat(actual.auctioneerCount()).isEqualTo(0);
        });
    }

    @Test
    void 채팅방이_없고_채팅_자격이_없는_사용자가_지정한_아이디에_해당하는_경매를_조회하면_채팅방_아이디_null과_채팅_불가를_반환한다() {
        // given
        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(경매_이미지_엔티티));

        // when
        final ReadAuctionDto actual = auctionService.readByAuctionId(채팅방이_없는_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.id()).isPositive();
            softAssertions.assertThat(actual.title()).isEqualTo(채팅방이_없는_경매.getTitle());
            softAssertions.assertThat(actual.description()).isEqualTo(채팅방이_없는_경매.getDescription());
            softAssertions.assertThat(actual.bidUnit()).isEqualTo(채팅방이_없는_경매.getBidUnit().getValue());
            softAssertions.assertThat(actual.startPrice()).isEqualTo(채팅방이_없는_경매.getStartPrice().getValue());
            softAssertions.assertThat(actual.lastBidPrice()).isNull();
            softAssertions.assertThat(actual.deleted()).isFalse();
            softAssertions.assertThat(actual.closingTime()).isEqualTo(채팅방이_없는_경매.getClosingTime());
            softAssertions.assertThat(actual.auctioneerCount()).isEqualTo(0);
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매가_없는_경매를_조회시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.readByAuctionId(존재하지_않는_경매_ID))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("지정한 아이디에 대한 경매를 찾을 수 없습니다.");
    }

    @Test
    void 첫번째_페이지의_경매_목록을_조회한다() {
        // given
        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(경매_이미지_엔티티));

        // when
        final ReadAuctionsDto actual = auctionService.readAllByCondition(
                PageRequest.of(0, 1, Sort.by(Order.desc("id"))),
                new ReadAuctionSearchCondition(null)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            final List<ReadAuctionDto> actualReadAuctionDtos = actual.readAuctionDtos();
            softAssertions.assertThat(actualReadAuctionDtos).hasSize(1);
            softAssertions.assertThat(actualReadAuctionDtos.get(0).title()).isEqualTo(채팅방이_있는_경매.getTitle());
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_삭제한다() {
        // when
        auctionService.deleteByAuctionId(채팅방이_없는_경매.getId(), 판매자.getId());

        // then
        final Optional<Auction> actual = auctionRepository.findById(채팅방이_없는_경매.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).contains(채팅방이_없는_경매);
            softAssertions.assertThat(actual.get().isDeleted()).isTrue();
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매가_없는_경매를_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.deleteByAuctionId(존재하지_않는_경매_ID, 판매자.getId()))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("지정한 아이디에 대한 경매를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_회원이_없는_경우_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.deleteByAuctionId(채팅방이_없는_경매.getId(), 존재하지_않는_사용자_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("회원 정보를 찾을 수 없습니다.");
    }

    @Test
    void 지정한_아이디에_해당하는_회원과_판매자가_일치하지_않는_경우_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionService.deleteByAuctionId(채팅방이_있는_경매.getId(), 구매자.getId()))
                .isInstanceOf(UserForbiddenException.class)
                .hasMessage("권한이 없습니다.");
    }

    @Test
    void 회원이_등록한_경매_목록을_조회한다() {
        // given
        given(imageProcessor.storeImageFiles(any())).willReturn(List.of(경매_이미지_엔티티));
        final PageRequest pageRequest = PageRequest.of(0, 3);

        // when
        final ReadAuctionsDto actual = auctionService.readAllByUserId(판매자.getId(), pageRequest);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.readAuctionDtos()).hasSize(3);
            softAssertions.assertThat(actual.readAuctionDtos().get(0).id()).isEqualTo(입찰이_존재하는_경매.getId());
            softAssertions.assertThat(actual.readAuctionDtos().get(1).id()).isEqualTo(채팅방이_없는_경매.getId());
            softAssertions.assertThat(actual.readAuctionDtos().get(2).id()).isEqualTo(채팅방이_있는_경매.getId());
        });
    }

    @Test
    void 회원이_참여한_경매_목록을_조회한다() {
        final PageRequest pageRequest = PageRequest.of(0, 3);
        final ReadAuctionsDto actual = auctionService.readAllByBidderId(구매자.getId(), pageRequest);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.readAuctionDtos()).hasSize(1);
            softAssertions.assertThat(actual.readAuctionDtos().get(0).id()).isEqualTo(입찰이_존재하는_경매.getId());
        });
    }
}
