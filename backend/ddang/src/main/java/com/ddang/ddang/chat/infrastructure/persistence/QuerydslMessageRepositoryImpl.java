package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ddang.ddang.chat.domain.QMessage.message;

@Repository
@RequiredArgsConstructor
public class QuerydslMessageRepositoryImpl implements QuerydslMessageRepository {

    private final JPAQueryFactory queryFactory;
    // TODO : 초기화 방식 주입 받을 수 있도록 변경
    private final BooleanBuilder booleanBuilder = new BooleanBuilder();

    @Override
    public List<Message> findMessagesAllByLastMessageId(
            final Long userId,
            final Long chatRoomId,
            final Long lastMessageId
    ) {
        return queryFactory
                .selectFrom(message)
                .where(
                        message.writer.id.eq(userId),
                        message.chatRoom.id.eq(chatRoomId),
                        isGreaterThanLastId(lastMessageId)
                )
                .orderBy(message.id.asc())
                .fetch();
    }

    private BooleanBuilder isGreaterThanLastId(final Long lastMessageId) {
        if (lastMessageId == null) {
            return null;
        }

        return booleanBuilder.or(message.id.gt(lastMessageId));
    }
}
