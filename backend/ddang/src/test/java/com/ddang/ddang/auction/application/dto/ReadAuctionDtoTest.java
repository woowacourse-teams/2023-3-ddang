package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.application.dto.fixture.ReadAuctionDtoFixture;
import com.ddang.ddang.configuration.IsolateDatabase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReadAuctionDtoTest extends ReadAuctionDtoFixture {

    @Test
    void 지정한_아이디에_해당하는_경매의_판매자_신뢰도가_null이라면_서비스에서_반환하는_dto에서_판매자_신뢰도를_나타내는_부분도_null이다() {
        // when
        final ReadAuctionDto actual = ReadAuctionDto.of(신뢰도가_null인_판매자의_경매, LocalDateTime.now());

        // then
        assertThat(actual.sellerReliability()).isNull();
    }
}
