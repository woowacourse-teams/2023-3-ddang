package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoomAndImageDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndImageQueryProjectionDto;
import com.ddang.ddang.chat.infrastructure.persistence.dto.QChatRoomAndImageQueryProjectionDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.chat.domain.QChatRoom.chatRoom;
import static com.ddang.ddang.image.domain.QAuctionImage.auctionImage;

@Repository
@RequiredArgsConstructor
public class QuerydslChatRoomAndImageRepositoryImpl implements QuerydslChatRoomAndImageRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChatRoomAndImageDto> findChatRoomById(final Long chatRoomId) {

        final ChatRoomAndImageQueryProjectionDto chatRoomAndImageQueryProjectionDto =
                queryFactory.select(new QChatRoomAndImageQueryProjectionDto(chatRoom, auctionImage))
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
                            .where(chatRoom.id.eq(chatRoomId))
                            .fetchOne();

        if (chatRoomAndImageQueryProjectionDto == null) {
            return Optional.empty();
        }

        return Optional.of(chatRoomAndImageQueryProjectionDto.toDto());
    }
}
