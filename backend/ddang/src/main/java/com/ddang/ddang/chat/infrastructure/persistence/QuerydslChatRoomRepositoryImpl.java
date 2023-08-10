package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomRepositoryImpl implements QuerydslChatRoomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoom> findAllByUserId(final Long userId) {
        return queryFactory.selectFrom(chatRoom)
                           .leftJoin(chatRoom.auction, auction).fetchJoin()
                           .where(isSellerOrWinner(userId))
                           .orderBy(chatRoom.id.desc())
                           .fetch();
    }

    private BooleanExpression isSellerOrWinner(final Long userId) {
        return (auction.seller.id.eq(userId))
                .or(chatRoom.buyer.id.eq(userId));
    }

    @Override
    public Optional<ChatRoom> findByAuctionId(final Long auctionId) {
        final ChatRoom findChatRoom = queryFactory.selectFrom(chatRoom)
                                                  .leftJoin(chatRoom.auction, auction).fetchJoin()
                                                  .where(auction.id.eq(auctionId))
                                                  .fetchOne();

        return Optional.ofNullable(findChatRoom);
    }
}
