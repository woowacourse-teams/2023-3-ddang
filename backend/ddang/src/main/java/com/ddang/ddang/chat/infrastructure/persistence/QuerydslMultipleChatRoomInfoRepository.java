package com.ddang.ddang.chat.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;
import static com.ddang.ddang.chat.domain.QMessage.message;
import static com.ddang.ddang.chat.domain.QReadMessageLog.readMessageLog;
import static java.util.Comparator.comparing;

import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.MultipleChatRoomInfoQueryProjectionDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.QMultipleChatRoomInfoQueryProjectionDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslMultipleChatRoomInfoRepository {

    private final JPAQueryFactory queryFactory;

    public List<MultipleChatRoomInfoDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId) {
        final List<MultipleChatRoomInfoQueryProjectionDto> unsortedDtos =
                queryFactory.select(new QMultipleChatRoomInfoQueryProjectionDto(
                                    chatRoom,
                                    message,
                                    countUnreadMessages(userId, chatRoom.id)
                            )).from(chatRoom)
                            .leftJoin(chatRoom.buyer).fetchJoin()
                            .leftJoin(chatRoom.auction, auction).fetchJoin()
                            .leftJoin(auction.seller).fetchJoin()
                            .leftJoin(auction.lastBid).fetchJoin()
                            .leftJoin(message).on(message.id.eq(
                                    JPAExpressions
                                            .select(message.id.max())
                                            .from(message)
                                            .where(message.chatRoom.id.eq(chatRoom.id))
                                            .groupBy(message.chatRoom.id)
                            )).fetchJoin()
                            .where(isSellerOrWinner(userId))
                            .fetch();

        return sortByLastMessageIdDesc(unsortedDtos);
    }

    private static JPQLQuery<Long> countUnreadMessages(final Long userId, final NumberPath<Long> chatRoomId) {
        return JPAExpressions.select(message.count())
                             .from(message)
                             .where(
                                     message.chatRoom.id.eq(chatRoomId),
                                     message.writer.id.ne(userId),
                                     message.id.gt(
                                             JPAExpressions
                                                     .select(readMessageLog.lastReadMessageId)
                                                     .from(readMessageLog)
                                                     .where(readMessageLog.reader.id.eq(userId), readMessageLog.chatRoom.id.eq(chatRoomId))
                                     )
                             );
    }

    private List<MultipleChatRoomInfoDto> sortByLastMessageIdDesc(
            final List<MultipleChatRoomInfoQueryProjectionDto> unsortedDtos
    ) {
        return unsortedDtos.stream()
                           .filter((MultipleChatRoomInfoQueryProjectionDto unsortedDto) ->
                                   Objects.nonNull(unsortedDto.message())
                           ).sorted(comparing(
                                (MultipleChatRoomInfoQueryProjectionDto unsortedDto) ->
                                        unsortedDto.message().getId()
                        ).reversed()
                ).map(MultipleChatRoomInfoQueryProjectionDto::toSortedDto)
                           .toList();
    }

    private BooleanExpression isSellerOrWinner(final Long userId) {
        return (auction.seller.id.eq(userId))
                .or(chatRoom.buyer.id.eq(userId));
    }
}
