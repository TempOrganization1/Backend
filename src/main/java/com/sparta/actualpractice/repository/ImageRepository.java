package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByAlbumOrderById(Album album);

    Image findTopByAlbumOrderById(Album album);
}
