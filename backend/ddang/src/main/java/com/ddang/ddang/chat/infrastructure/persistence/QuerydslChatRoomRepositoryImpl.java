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
import static com.ddang.ddang.chat.domain.QMessage.message;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomRepositoryImpl implements QuerydslChatRoomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoom> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId) {
        return queryFactory
                .selectFrom(chatRoom)
                .leftJoin(chatRoom.buyer).fetchJoin()
                .leftJoin(chatRoom.auction, auction).fetchJoin()
                .leftJoin(auction.seller).fetchJoin()
                .leftJoin(auction.auctionImages).fetchJoin()
                .leftJoin(auction.lastBid).fetchJoin()
                .where(isSellerOrWinner(userId))
                .orderBy(message.id.max().desc())
                .leftJoin(message).on(message.chatRoom.id.eq(chatRoom.id))
                .groupBy(chatRoom.id)
                .fetch();
    }

    private BooleanExpression isSellerOrWinner(final Long userId) {
        return (auction.seller.id.eq(userId))
                .or(chatRoom.buyer.id.eq(userId));
    }

    @Override
    public Optional<ChatRoom> findChatRoomById(final Long chatRoomId) {
        final ChatRoom findChatRoom = queryFactory.selectFrom(chatRoom)
                                                  .leftJoin(chatRoom.buyer).fetchJoin()
                                                  .leftJoin(chatRoom.auction, auction).fetchJoin()
                                                  .leftJoin(auction.seller).fetchJoin()
                                                  .leftJoin(auction.auctionImages).fetchJoin()
                                                  .leftJoin(auction.lastBid).fetchJoin()
                                                  .where(chatRoom.id.eq(chatRoomId))
                                                  .fetchFirst();

        return Optional.ofNullable(findChatRoom);
    }

    @Override
    public Optional<Long> findChatRoomIdByAuctionId(final Long auctionId) {
        final ChatRoom findChatRoom = queryFactory.selectFrom(chatRoom)
                                                  .where(chatRoom.auction.id.eq(auctionId))
                                                  .fetchFirst();
        if (findChatRoom == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(findChatRoom.getId());
    }
}
