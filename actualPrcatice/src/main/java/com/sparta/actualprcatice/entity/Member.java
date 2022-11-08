package com.sparta.actualprcatice.entity;

import com.example.consolelog.dto.requestDto.MemberReqeustDto;
import com.example.consolelog.util.TimeStamped;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String image = "defalut";

    @Column
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.ROLE_USER;

    @OneToMany(mappedBy = "member")
    private List<Board> boardList;

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList;

    public Member(MemberReqeustDto memberReqeustDto, String password) {
        this.name = memberReqeustDto.getName();
        this.password = password;
        this.nickname = memberReqeustDto.getNickname();
    }

}
