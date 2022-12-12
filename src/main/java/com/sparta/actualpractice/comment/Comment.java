package com.sparta.actualpractice.comment;


import com.sparta.actualpractice.album.Album;
import com.sparta.actualpractice.member.Member;
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

    @Column(nullable = false, length = 300)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM_ID")
    private Album album;

    public Comment(String content, Album album, Member member){

        this.content = content;
        this.album = album;
        this.member = member;
    }

    public void updateContent(String content) {

        this.content = content;
    }
}
