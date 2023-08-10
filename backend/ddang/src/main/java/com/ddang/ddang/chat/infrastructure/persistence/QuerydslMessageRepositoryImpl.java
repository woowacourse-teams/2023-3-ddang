package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ddang.ddang.chat.domain.QMessage.message;

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
}
