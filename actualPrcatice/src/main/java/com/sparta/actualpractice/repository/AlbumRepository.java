package com.sparta.actualpractice.repository;


import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByPartyOrderByCreatedAtDesc(Party party);
}

