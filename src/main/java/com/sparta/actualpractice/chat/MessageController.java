package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ResponseBody
    @GetMapping("/chatrooms/{chatRoom_id}")
    public ResponseEntity<?> readMessages(@PathVariable("chatRoom_id") Long chatRoomId) {

        System.out.println(" 1번");
        return messageService.readMessages(chatRoomId);
    }

//    @ResponseBody
//    @PostMapping("/chartrooms")
//}

    @MessageMapping(value = {"/chatrooms/{chatRoom_id}"})
    public void addMessage(@RequestBody MessageRequestDto messageRequestDto,
                           @DestinationVariable("chatRoom_id") Long chatRoomId,
                           @Header("Authorization") String token) {

        System.out.println("chatRoomId = " + chatRoomId);
        System.out.println("messageRequestDto = " + messageRequestDto.getContent());
        messageService.sendMessage(messageRequestDto, chatRoomId, token);
    }

}
