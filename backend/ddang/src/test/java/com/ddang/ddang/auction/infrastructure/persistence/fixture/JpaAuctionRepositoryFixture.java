package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class JpaAuctionRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    private Instant 시간 = Instant.parse("2023-07-08T22:21:20Z");
    private ZoneId 위치 = ZoneId.of("UTC");

    protected Auction 저장하기_전_경매_엔티티 = Auction.builder()
                                                        .title("제목")
                                                        .description("내용")
                                                        .bidUnit(new BidUnit(1_000))
                                                        .startPrice(new Price(1_000))
                                                        .closingTime(LocalDateTime.now())
                                                        .build();
    protected Auction 저장된_경매_엔티티 = Auction.builder()
                                          .title("경매 상품 1")
                                          .description("이것은 경매 상품 1 입니다.")
                                          .bidUnit(new BidUnit(1_000))
                                          .startPrice(new Price(1_000))
                                          .closingTime(시간.atZone(위치).toLocalDateTime())
                                          .build();
    protected Auction 삭제된_경매_엔티티 = Auction.builder()
                                          .title("경매 상품 1")
                                          .description("이것은 경매 상품 1 입니다.")
                                          .bidUnit(new BidUnit(1_000))
                                          .startPrice(new Price(1_000))
                                          .closingTime(시간.atZone(위치).toLocalDateTime())
                                          .build();

    @BeforeEach
    void setUp() {
        삭제된_경매_엔티티.delete();

        auctionRepository.saveAll(List.of(저장된_경매_엔티티, 삭제된_경매_엔티티));

        em.flush();
        em.clear();
    }
}
