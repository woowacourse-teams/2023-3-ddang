package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.dto.ChatRoomAndMessageAndImageDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageAndImageQueryProjectionDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.QChatRoomAndMessageAndImageQueryProjectionDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;
import static com.ddang.ddang.chat.domain.QMessage.message;
import static com.ddang.ddang.image.domain.QAuctionImage.auctionImage;
import static java.util.Comparator.comparing;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomAndMessageAndImageRepository {

    private final JPAQueryFactory queryFactory;

    public List<ChatRoomAndMessageAndImageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId) {
        final List<ChatRoomAndMessageAndImageQueryProjectionDto> unsortedDtos =
                queryFactory.select(new QChatRoomAndMessageAndImageQueryProjectionDto(chatRoom, message, auctionImage))
                            .from(chatRoom)
                            .leftJoin(chatRoom.buyer).fetchJoin()
                            .leftJoin(chatRoom.auction, auction).fetchJoin()
                            .leftJoin(auction.seller).fetchJoin()
                            .leftJoin(auctionImage).on(auctionImage.id.eq(
                                    JPAExpressions
                                            .select(auctionImage.id.min())
                                            .from(auctionImage)
                                            .where(auctionImage.auction.id.eq(auction.id))
                                            .groupBy(auctionImage.auction.id)
                            )).fetchJoin()
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
