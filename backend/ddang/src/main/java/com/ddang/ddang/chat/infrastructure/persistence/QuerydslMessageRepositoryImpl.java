package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ddang.ddang.category.domain.QCategory.category;
import static com.ddang.ddang.chat.domain.QMessage.message;
import java.util.List;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;
import static com.ddang.ddang.chat.domain.QMessage.message;
import static com.ddang.ddang.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class QuerydslMessageRepositoryImpl implements QuerydslMessageRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Message> findLastMessageByChatRoomId(final Long chatRoomId) {
        final Message findLastMessage = queryFactory.selectFrom(message)
                                                    .leftJoin(message.chatRoom).fetchJoin()
                                                    .where(message.chatRoom.id.eq(chatRoomId))
                                                    .orderBy(message.id.desc())
                                                    .fetchFirst();

        return Optional.ofNullable(findLastMessage);
    }

    public List<Message> findMessagesAllByLastMessageId(
            final Long userId,
            final Long chatRoomId,
            final Long lastMessageId
    ) {
        return queryFactory
                .selectFrom(message)
                .leftJoin(message.chatRoom, chatRoom).fetchJoin()
                .leftJoin(chatRoom.auction, auction).fetchJoin()
                .leftJoin(auction.subCategory, category).fetchJoin()
                .leftJoin(category.mainCategory).fetchJoin()
                .leftJoin(auction.auctionImages).fetchJoin()
                .leftJoin(message.receiver, user).fetchJoin()
                .leftJoin(message.writer).fetchJoin()
                .where(
                        message.writer.id.eq(userId)
                                         .or(message.receiver.id.eq(userId)),
                        message.chatRoom.id.eq(chatRoomId),
                        isGreaterThanLastId(lastMessageId)
                )
                .orderBy(message.id.asc())
                .fetch();
    }

    private BooleanExpression isGreaterThanLastId(final Long lastMessageId) {
        if (lastMessageId == null) {
            return null;
        }

        return message.id.gt(lastMessageId);
    }
}
