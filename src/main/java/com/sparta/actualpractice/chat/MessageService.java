package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.entity.ChatRoom;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Message;
import com.sparta.actualpractice.repository.MessageRepository;
import com.sparta.actualpractice.repository.ChatRoomRepository;
import com.sparta.actualpractice.repository.MemberRepository;
import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String MESSAGE = "MESSAGE";
    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<?> readMessages(Long chatRoomId) {

        List<MessageResponseDto> messageResponseDtoList = messageRepository.findTop100ByChatRoomIdOrderByCreatedAtDesc(chatRoomId).stream()
                .map(MessageResponseDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(messageResponseDtoList, HttpStatus.OK);
    }

    @Transactional
    public void sendMessage(MessageRequestDto messageRequestDto, Long chatRoomId, String token) {

        String email = tokenProvider.decodedMemberName(token);
        System.out.println("email = " + email);

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("멤버를 찾을 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new NullPointerException("채팅룸을 찾을 수 없습니다."));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = simpleDateFormat.format(date);

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .content(messageRequestDto.getContent())
                .createdAt(dateResult)
                .member(member)
                .build();

        messageRepository.save(message);
        MessageResponseDto messageResponseDto = new MessageResponseDto(message);
        template.convertAndSend("/sub/chatrooms/" + messageResponseDto.getChatRoomId(), messageResponseDto);
    }
}
