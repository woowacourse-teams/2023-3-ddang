package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.CreateMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/chattings")
@RequiredArgsConstructor
public class ChatRoomController {

    private final MessageService messageService;

    @PostMapping("/{chatRoomId}/messages")
    public ResponseEntity<CreateMessageResponse> create(
            @PathVariable final Long chatRoomId,
            @RequestBody @Valid final CreateMessageRequest request
    ) {
        final Long messageId = messageService.create(CreateMessageDto.from(chatRoomId, request));
        final CreateMessageResponse response = new CreateMessageResponse(messageId);

        return ResponseEntity.created(URI.create("/chattings/" + chatRoomId + "/messages/" + messageId))
                             .body(response);
    }
}
