package com.ddang.ddang.chat.application;

import com.ddang.ddang.chat.application.event.CreateReadMessageLogEvent;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LastReadMessageLogEventListener {

    private final LastReadMessageLogService lastReadMessageLogService;

    @EventListener
    @Transactional
    public void create(final CreateReadMessageLogEvent createReadMessageLogEvent) {
        lastReadMessageLogService.create(createReadMessageLogEvent);
    }

    @EventListener
    @Transactional
    public void update(final UpdateReadMessageLogEvent updateReadMessageLogEvent) {
        lastReadMessageLogService.update(updateReadMessageLogEvent);
    }
}
