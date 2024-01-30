package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.event.CreateReadMessageLogEvent;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import com.ddang.ddang.chat.application.exception.ReadMessageLogNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class LastReadMessageLogEventListener {

    private final LastReadMessageLogService lastReadMessageLogService;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(final CreateReadMessageLogEvent createReadMessageLogEvent) {
        try {
            lastReadMessageLogService.create(createReadMessageLogEvent);
        } catch (final IllegalArgumentException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update(final UpdateReadMessageLogEvent updateReadMessageLogEvent) {
        try {
            lastReadMessageLogService.update(updateReadMessageLogEvent);
        } catch (final ReadMessageLogNotFoundException ex) {
            log.error("exception type : {}, ", ex.getClass().getSimpleName(), ex);
        }
    }
}
