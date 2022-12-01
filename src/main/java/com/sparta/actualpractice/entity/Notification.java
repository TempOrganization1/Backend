package com.sparta.actualpractice.entity;


import com.sparta.actualpractice.dto.response.NotificationResponseDto;
import com.sparta.actualpractice.util.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Notification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    private String partyName;

    private String title;

    private String writer;

    private String content;

    private String ProfileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public Notification(Member member, NotificationResponseDto notificationResponseDto) {


        this.member = member;
        this.isRead = false;
        this.partyName = notificationResponseDto.getPartyName();
        this.title = notificationResponseDto.getTitle();
        this.writer = notificationResponseDto.getWriter();
        this.content = notificationResponseDto.getContent();
        this.ProfileUrl = notificationResponseDto.getProfileUrl();
        this.message = notificationResponseDto.getMessage();
        this.url = notificationResponseDto.getUrl();
        this.alarmType = notificationResponseDto.getAlarmType();
    }

    public void read() {

        this.isRead = true;
    }
}
