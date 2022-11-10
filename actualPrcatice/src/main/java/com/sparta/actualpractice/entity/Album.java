package com.sparta.actualpractice.entity;

import com.sparta.actualpractice.dto.request.AlbumRequestDto;
import com.sparta.actualpractice.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Album extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @OneToMany(mappedBy = "album")
    private List<Comment> commentList;


}
