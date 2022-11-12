package com.sparta.actualpractice.entity;


import com.sparta.actualpractice.dto.request.CommentRequestDto;
import com.sparta.actualpractice.util.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM_ID")
    private Album album;

    public Comment(CommentRequestDto commentRequestDto, Album album, Member member){

        this.content = commentRequestDto.getContent();
        this.album = album;
        this.member = member;
    }

    public void update(CommentRequestDto commentRequestDto) {

        this.content = commentRequestDto.getContent();
    }
}
