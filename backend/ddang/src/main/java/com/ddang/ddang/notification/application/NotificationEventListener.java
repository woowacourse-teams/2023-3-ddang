package com.ddang.ddang.notification.application;

import com.ddang.ddang.chat.application.dto.MessageDto;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void sendMessageNotification(final MessageNotificationEvent messageNotificationEvent) {
        final MessageDto messageDto = messageNotificationEvent.messageDto();
        notificationService.send(CreateNotificationDto.from(messageDto));
    }
}
