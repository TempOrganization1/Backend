package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.response.NotificationResponseDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Notification;
import com.sparta.actualpractice.repository.EmitterRepository;
import com.sparta.actualpractice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) {

        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

            emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
            emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

            sendToClient(emitter, emitterId, "EventStream Created. [userId=" + memberId + "]");

            if (!lastEventId.isEmpty())
                sendLostData(lastEventId, memberId, emitter);

        return emitter;
    }

    public ResponseEntity<?> getAllNotifications(Member member) {

        List<Notification> notifications = notificationRepository.findAllByMemberOrderByIdDesc(member);
        List<NotificationResponseDto> notificationResponseDto = new ArrayList<>();

        for(Notification notification : notifications)

            notificationResponseDto.add(new NotificationResponseDto(notification));

        return new ResponseEntity<>(notificationResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getCountUnreadNotifications(Member member) {

        Long cnt = notificationRepository.countByIsReadFalseAndMember(member);

        return new ResponseEntity<>(cnt, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateReadNotification(Long notificationId, Member member) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NullPointerException("해당 알림이 존재하지 않습니다."));

        if(validateNotification(member, notificationId))
            throw new IllegalArgumentException("해당 알람의 권한이 없습니다.");

        notification.read();

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteNotification(Long notificationId , Member member) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NullPointerException("해당 알림이 존재하지 않습니다."));

        if(validateNotification(member, notificationId))
            throw new IllegalArgumentException("해당 알람의 권한이 없습니다.");

        notificationRepository.delete(notification);

        return new ResponseEntity<>("단일 알림 삭제 완료", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> deleteAllNotification(Member member) {

        notificationRepository.deleteAllByMember(member);

        return new ResponseEntity<>("알림 전체 삭제 완료", HttpStatus.OK);
    }

    //문자열 입출력 예외처리 IOException
    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {

        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("sse")
                    .data(data));
            log.info(" 알림 전송 완료 {}", emitterId);
        } catch (IOException exception) {

            emitterRepository.deleteById(emitterId);
            log.info("{}", exception.getMessage());
        }
    }

    private void sendLostData(String lastEventId, Long memberId, SseEmitter emitter) {

        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));

        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        log.info("유실 데이터 전송 완료 ,{}", lastEventId);
    }
    @Async
    public void send(Member member, NotificationResponseDto notificationResponseDto) {

        Notification notification = new Notification(member, notificationResponseDto);
        notificationRepository.save(notification);

        String receiverId = String.valueOf(member.getId());

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponseDto.from(notification));
                    log.info("알림 전송 내용 : {}", notification.getMessage());
                }
        );
    }

    public boolean validateNotification(Member member, Long notificationId) {

        return !notificationRepository.existsByMemberAndId(member, notificationId);
    }


}
