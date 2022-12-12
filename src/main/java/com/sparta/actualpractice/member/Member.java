package com.sparta.actualpractice.member;


import com.sparta.actualpractice.album.Album;
import com.sparta.actualpractice.comment.Comment;

import com.sparta.actualpractice.memberParty.MemberParty;
import com.sparta.actualpractice.notification.Notification;
import com.sparta.actualpractice.party.Admin;
import com.sparta.actualpractice.schedule.Schedule;
import com.sparta.actualpractice.util.TimeStamped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = true)
    private Long googleId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String imageUrl;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Album> albumList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<MemberParty> memberPartyList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Notification> notificationList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Admin> adminList;

    public Member(String email, String name, String password) {

        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void updateImage(String imageUrl) {

        this.imageUrl = imageUrl;
    }

    public void updateName(String name) {

        this.name = name;
    }
}
