package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.event.CreateReadMessageLogEvent;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import com.ddang.ddang.chat.application.exception.ReadMessageLogNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LastReadMessageLogService {

    private final ReadMessageLogRepository readMessageLogRepository;

    @Transactional
    public void create(final CreateReadMessageLogEvent createReadMessageLogEvent) {
        final ChatRoom chatRoom = createReadMessageLogEvent.chatRoom();
        final User buyer = chatRoom.getBuyer();
        final User seller = chatRoom.getAuction().getSeller();
        final ReadMessageLog buyerReadMessageLog = new ReadMessageLog(chatRoom, buyer);
        final ReadMessageLog sellerReadMessageLog = new ReadMessageLog(chatRoom, seller);

        readMessageLogRepository.saveAll(List.of(buyerReadMessageLog, sellerReadMessageLog));
    }

    @Transactional
    public void update(final UpdateReadMessageLogEvent updateReadMessageLogEvent) {
        final User reader = updateReadMessageLogEvent.reader();
        final ChatRoom chatRoom = updateReadMessageLogEvent.chatRoom();
        final ReadMessageLog messageLog = readMessageLogRepository.findBy(reader.getId(), chatRoom.getId())
                                                                  .orElseThrow(() ->
                                                                          new ReadMessageLogNotFoundException(
                                                                                  "메시지 조회 로그가 존재하지 않습니다."
                                                                          ));

        messageLog.updateLastReadMessage(updateReadMessageLogEvent.lastReadMessage().getId());
    }
}
