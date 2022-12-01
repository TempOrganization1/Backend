package com.sparta.actualpractice.entity;


import com.sparta.actualpractice.dto.request.MemberRequestDto;

import com.sparta.actualpractice.dto.response.OAuth2memberInfoDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTIFICATION_ID")
    private Notification notification;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Admin> adminList;

    public Member(MemberRequestDto memberRequestDto, String password) {

        this.email = memberRequestDto.getEmail();
        this.password = password;
        this.name = memberRequestDto.getName();
    }

    public void updateImage(String imageUrl) {

        this.imageUrl = imageUrl;
    }
}
