package com.ddang.ddang.chat.infrastructure.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomRepositoryImpl implements QuerydslChatRoomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findChatRoomIdByAuctionId(final Long auctionId) {
        final Long chatRoomId = queryFactory.select(chatRoom.id)
                                            .from(chatRoom)
                                            .where(chatRoom.auction.id.eq(auctionId))
                                            .fetchFirst();

        return Optional.ofNullable(chatRoomId);
    }
}
