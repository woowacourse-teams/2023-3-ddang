package com.ddang.ddang.chat.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;

import com.ddang.ddang.chat.domain.dto.ChatRoomAndImageDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndImageQueryProjectionDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.QChatRoomAndImageQueryProjectionDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomAndImageRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<ChatRoomAndImageDto> findChatRoomById(final Long chatRoomId) {

        final ChatRoomAndImageQueryProjectionDto chatRoomAndImageQueryProjectionDto =
                queryFactory.select(new QChatRoomAndImageQueryProjectionDto(chatRoom))
                            .from(chatRoom)
                            .leftJoin(chatRoom.buyer).fetchJoin()
                            .leftJoin(chatRoom.auction, auction).fetchJoin()
                            .leftJoin(auction.seller).fetchJoin()
                            .leftJoin(auction.lastBid).fetchJoin()
                            .where(chatRoom.id.eq(chatRoomId))
                            .fetchOne();

        if (chatRoomAndImageQueryProjectionDto == null) {
            return Optional.empty();
        }

        return Optional.of(chatRoomAndImageQueryProjectionDto.toDto());
    }
}
