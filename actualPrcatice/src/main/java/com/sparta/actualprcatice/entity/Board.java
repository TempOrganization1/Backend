package com.sparta.actualprcatice.entity;

import com.example.consolelog.dto.requestDto.BoardRequestDto;
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
public class Board extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private Boolean state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Heart> heartList;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> imagesList;


    // 게시물 작성 시 사용.
    public Board(Member member){
        this.title = null;
        this.content = null;
        this.member = member;
        this.state = false;
    }

    // 게시물 업로드, 수정 시 사용
    public void update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.state = true;
    }
}
