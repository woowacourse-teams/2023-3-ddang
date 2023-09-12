package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomRepositoryImpl implements QuerydslChatRoomRepository {

    private final JPAQueryFactory queryFactory;

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
