package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.member.MemberRepository;
import com.sparta.actualpractice.security.TokenProvider;
import com.sparta.actualpractice.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
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
    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public ResponseEntity<?> readMessages(Long chatRoomId) {

//        List<MessageResponseDto> messageResponseDtoList = messageRepository.findTop500ByChatRoomIdOrderByCreatedAtAsc(chatRoomId).stream()
//                .map(MessageResponseDto::new).collect(Collectors.toList());

        HashOperations<String, String, List<MessageResponseDto>> operations = redisTemplate.opsForHash();

        List<MessageResponseDto> messageResponseDtoList = new ArrayList<>();
        List<MessageResponseDto> tempDto1 = operations.get(MESSAGE, String.valueOf(chatRoomId));
        List<MessageResponseDto> tempDto2 = messageRepository.findTop500ByChatRoomIdOrderByCreatedAtAsc(chatRoomId).stream().map(MessageResponseDto::new)
                .collect(Collectors.toList());

        if (tempDto1 != null) {
            messageResponseDtoList.addAll(tempDto1);
        }

        messageResponseDtoList.addAll(tempDto2);

        return new ResponseEntity<>(messageResponseDtoList, HttpStatus.OK);
    }

    @Transactional
    public void sendMessage(MessageRequestDto messageRequestDto, Long chatRoomId, String jwtToken) {

        String email = tokenProvider.decodedEmail(jwtToken);

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("멤버를 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new NullPointerException("채팅룸을 찾을 수 없습니다."));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

        MessageResponseDto messageResponseDto = new MessageResponseDto(message);

        redisTemplate.convertAndSend(channelTopic.getTopic(), messageRequestDto);

        HashOperations<String, String, List<MessageResponseDto>> operations = redisTemplate.opsForHash();

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MessageResponseDto.class));

        List<MessageResponseDto> messageResponseDtoList = operations.get(MESSAGE, chatRoomId);

        if (messageResponseDtoList == null)
            messageResponseDtoList = new ArrayList<>();

        messageResponseDtoList.add(0, messageResponseDto);

        operations.put(MESSAGE, String.valueOf(chatRoomId), messageResponseDtoList);

        if (messageResponseDtoList.size() >= 500)
            redisToMysql(String.valueOf(chatRoomId), messageResponseDtoList);
    }

    private void redisToMysql(String chatRoomId, List<MessageResponseDto> messageResponseDtoList) {

        HashOperations<String, String, List<MessageResponseDto>> operations = redisTemplate.opsForHash();

        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(chatRoomId)).orElseThrow(() -> new NullPointerException("해당 채팅룸을 찾을 수 없습니다."));

        log.info("data num : " + messageResponseDtoList.size());

        messageResponseDtoList.sort(Comparator.comparing(MessageResponseDto::getCreatedAt));

        for (MessageResponseDto messageResponseDto : messageResponseDtoList) {

            Member member = memberRepository.findByName(messageResponseDto.getMemberName()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

            messageRepository.save(Message.builder()
                    .chatRoom(chatRoom)
                    .content(messageResponseDto.getContent())
                    .member(member)
                    .createdAt(messageResponseDto.getCreatedAt())
                    .build());
        }

        operations.delete(MESSAGE, chatRoomId);
    }
}
