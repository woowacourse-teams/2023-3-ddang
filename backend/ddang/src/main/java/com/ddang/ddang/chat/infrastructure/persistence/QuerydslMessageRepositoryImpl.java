package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ddang.ddang.chat.domain.QMessage.message;

@Repository
@RequiredArgsConstructor
public class QuerydslMessageRepositoryImpl implements QuerydslMessageRepository {

    private final JPAQueryFactory queryFactory;

    public List<Message> findMessagesAllByLastMessageId(
            final Long readUserId,
            final Long chatRoomId,
            final Long lastMessageId
    ) {
        return queryFactory
                .selectFrom(message)
                .where(
                        message.writer.id.eq(readUserId)
                                         .or(message.receiver.id.eq(readUserId)),
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
