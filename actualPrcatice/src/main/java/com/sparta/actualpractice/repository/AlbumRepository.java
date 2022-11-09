package com.sparta.actualpractice.repository;


import com.sparta.actualpractice.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}

