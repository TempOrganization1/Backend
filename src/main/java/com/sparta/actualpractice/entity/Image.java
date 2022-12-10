package com.sparta.actualpractice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM_ID")
    private Album album;

    public Image(Album album, String imageUrl) {

        this.album = album;
        this.imageUrl = imageUrl;
    }
}
