package com.ddang.ddang.chat.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class QuerydslChatRoomAndImageRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private JpaAuctionRepository jpaAuctionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    protected AuctionImage 경매_대표_이미지;
    protected ChatRoom 채팅방;

    @BeforeEach
    void setUp() {
        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        final ProfileImage 프로필_이미지 = new ProfileImage("upload.png", "store.png");
        final User 판매자 = User.builder()
                              .name("판매자")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7d))
                              .oauthId("12345")
                              .build();
        final User 구매자 = User.builder()
                              .name("구매자")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7d))
                              .oauthId("12346")
                              .build();
        final AuctionImage 대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("대표 이미지가_아닌_경매_이미지.png", "대표 이미지가_아닌_경매_이미지.png");
        final Auction 경매 = Auction.builder()
                                  .seller(판매자)
                                  .title("맥북")
                                  .description("맥북 팔아요")
                                  .subCategory(전자기기_서브_노트북_카테고리)
                                  .startPrice(new Price(10_000))
                                  .bidUnit(new BidUnit(1_000))
                                  .closingTime(LocalDateTime.now())
                                  .build();
        final Bid 입찰 = new Bid(경매, 구매자, new BidPrice(15_000));

        경매_대표_이미지 = new AuctionImage("경매_대표_이미지.png", "경매_대표_이미지.png");
        채팅방 = new ChatRoom(경매, 구매자);

        final AuctionRepository auctionRepository = new AuctionRepositoryImpl(
                jpaAuctionRepository,
                new QuerydslAuctionRepository(queryFactory)
        );
        auctionRepository.save(경매);

        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        categoryRepository.save(전자기기_카테고리);
        userRepository.saveAll(List.of(판매자, 구매자));
        경매.addAuctionImages(List.of(경매_대표_이미지, 대표_이미지가_아닌_경매_이미지));
        bidRepository.save(입찰);
        경매.updateLastBid(입찰);
        chatRoomRepository.save(채팅방);

        em.flush();
        em.clear();
    }
}
