package com.sparta.actualpractice.notification;

import com.sparta.actualpractice.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    //memberdetails 넣기

    // api 알림 구독
    @GetMapping(value = "/subscriptions", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam(required = false, defaultValue = "") String lastEventId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return notificationService.subscribe(memberDetails.getMember().getId(), lastEventId);
    }

    //api 알림 조회
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return notificationService.getAllNotifications(memberDetails.getMember());
    }

    //알림 조회 - 구독자가 현재 읽지않은 알림 갯수
    @GetMapping(value = "/notifications/count")
    public ResponseEntity<?> getCountUnreadNotifications(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return notificationService.getCountUnreadNotifications(memberDetails.getMember());
    }

    //api 알림 읽음 처리
    @PutMapping("/notifications/{notificationId}")
    public ResponseEntity<?> updateReadNotification (@PathVariable Long notificationId , @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return notificationService.updateReadNotification(notificationId, memberDetails.getMember());
    }

    //api 단일 알림 삭제
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return notificationService.deleteNotification(notificationId, memberDetails.getMember());
    }

    //api 알림 전체 삭제
    @DeleteMapping("/notifications")
    public ResponseEntity<?> deleteAllNotification(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return notificationService.deleteAllNotification(memberDetails.getMember());
    }
}
