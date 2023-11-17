package com.ddang.ddang.chat.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;
import static com.ddang.ddang.chat.domain.QMessage.message;
import static com.ddang.ddang.chat.domain.QReadMessageLog.readMessageLog;
import static com.ddang.ddang.image.domain.QAuctionImage.auctionImage;
import static java.util.Comparator.comparing;

import com.ddang.ddang.chat.domain.dto.ChatRoomAndMessageAndImageDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageAndImageQueryProjectionDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.QChatRoomAndMessageAndImageQueryProjectionDto;
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
public class QuerydslChatRoomAndMessageAndImageRepository {

    private final JPAQueryFactory queryFactory;

    public List<ChatRoomAndMessageAndImageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId) {
        final List<ChatRoomAndMessageAndImageQueryProjectionDto> unsortedDtos =
                queryFactory.select(new QChatRoomAndMessageAndImageQueryProjectionDto(
                                    chatRoom,
                                    message,
                                    auctionImage,
                                    countUnreadMessages(userId, chatRoom.id)
                            )).from(chatRoom)
                            .leftJoin(chatRoom.buyer).fetchJoin()
                            .leftJoin(chatRoom.auction, auction).fetchJoin()
                            .leftJoin(auction.seller).fetchJoin()
                            .leftJoin(auctionImage).on(auctionImage.id.eq(
                                    JPAExpressions
                                            .select(auctionImage.id.min())
                                            .from(auctionImage)
                                            .where(auctionImage.auction.id.eq(auction.id))
                            )).fetchJoin()
                            .leftJoin(auction.lastBid).fetchJoin()
                            .leftJoin(message).on(message.id.eq(
                                    JPAExpressions
                                            .select(message.id.max())
                                            .from(message)
                                            .where(message.chatRoom.id.eq(chatRoom.id))
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

    private List<ChatRoomAndMessageAndImageDto> sortByLastMessageIdDesc(
            final List<ChatRoomAndMessageAndImageQueryProjectionDto> unsortedDtos
    ) {
        return unsortedDtos.stream()
                           .filter((ChatRoomAndMessageAndImageQueryProjectionDto unsortedDto) ->
                                   Objects.nonNull(unsortedDto.message())
                           ).sorted(comparing(
                                (ChatRoomAndMessageAndImageQueryProjectionDto unsortedDto) ->
                                        unsortedDto.message().getId()
                        ).reversed()
                ).map(ChatRoomAndMessageAndImageQueryProjectionDto::toSortedDto)
                           .toList();
    }

    private BooleanExpression isSellerOrWinner(final Long userId) {
        return (auction.seller.id.eq(userId))
                .or(chatRoom.buyer.id.eq(userId));
    }
}
