package com.sparta.actualpractice.notification;

import com.sparta.actualpractice.album.Album;
import com.sparta.actualpractice.comment.Comment;
import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long id;
    private String message;
    private Boolean status;
    private String partyName;
    private String title;
    private String writer;
    private String content;
    private String profileUrl;
    private String url;
    private AlarmType alarmType;

    public static NotificationResponseDto from(Notification notification) {

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .status(notification.getIsRead())
                .build();
    }

    public NotificationResponseDto(Album album, Comment comment, AlarmType alarmType) {

        this.partyName = album.getParty().getName();
        this.url = album.getParty().getId() + "/" + "album" + "/" + album.getId();
        this.writer = comment.getMember().getName();
        this.message = "회원님이 작성한 " + partyName + " 앨범에 " + writer + " 님이 댓글을 달았습니다.";
        this.content = comment.getContent();
        this.profileUrl = comment.getMember().getImageUrl();
        this.alarmType = alarmType;
    }

    public NotificationResponseDto(Schedule schedule, Member member, AlarmType alarmType) {

        this.partyName = schedule.getParty().getName();
        this.title = schedule.getTitle();
        this.writer = member.getName();
        this.message = "회원님이 작성한 " + partyName + " 일정에 " + writer + " 님이 참가하였습니다.";
        this.url = schedule.getParty().getId() + "/" + "scheduledetail" + "/" + schedule.getId();
        this.alarmType = alarmType;
    }

    public NotificationResponseDto(Notification notification) {

        this.id = notification.getId();
        this.status = notification.getIsRead();
        this.partyName = notification.getPartyName();
        this.title = notification.getTitle();
        this.writer = notification.getWriter();
        this.content = notification.getContent();
        this.profileUrl = notification.getProfileUrl();
        this.message = notification.getMessage();
        this.url = notification.getUrl();
        this.alarmType = notification.getAlarmType();
    }
}
