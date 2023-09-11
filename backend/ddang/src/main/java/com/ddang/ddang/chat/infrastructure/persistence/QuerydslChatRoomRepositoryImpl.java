package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageQueryProjectionDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.QChatRoomAndMessageQueryProjectionDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;
import static com.ddang.ddang.chat.domain.QMessage.message;
import static java.util.Comparator.comparing;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomRepositoryImpl implements QuerydslChatRoomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoomAndMessageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId) {
        final List<ChatRoomAndMessageQueryProjectionDto> unsortedDtos = queryFactory
                .select(new QChatRoomAndMessageQueryProjectionDto(chatRoom, message))
                .from(chatRoom)
                .leftJoin(chatRoom.buyer).fetchJoin()
                .leftJoin(chatRoom.auction, auction).fetchJoin()
                .leftJoin(auction.seller).fetchJoin()
                .leftJoin(auction.auctionImages).fetchJoin()
                .leftJoin(auction.lastBid).fetchJoin()
                .leftJoin(message).on(message.id.eq(
                        JPAExpressions
                                .select(message.id.max())
                                .from(message)
                                .where(message.chatRoom.id.eq(chatRoom.id))
                ))
                .where(isSellerOrWinner(userId))
                .orderBy(message.id.max().desc())
                .groupBy(chatRoom.id)
                .fetch();

        return sortByLastMessageIdDesc(unsortedDtos);
    }

    private List<ChatRoomAndMessageDto> sortByLastMessageIdDesc(
            final List<ChatRoomAndMessageQueryProjectionDto> unsortedDtos
    ) {
        return unsortedDtos.stream()
                           .sorted(comparing(
                                   (ChatRoomAndMessageQueryProjectionDto unsortedDto) -> unsortedDto.message().getId()
                                   ).reversed()
                           ).map(ChatRoomAndMessageQueryProjectionDto::toSortedDto)
                           .toList();
    }

    private BooleanExpression isSellerOrWinner(final Long userId) {
        return (auction.seller.id.eq(userId))
                .or(chatRoom.buyer.id.eq(userId));
    }

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
