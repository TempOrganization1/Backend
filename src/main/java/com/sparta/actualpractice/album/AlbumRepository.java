package com.sparta.actualpractice.album;


import com.sparta.actualpractice.album.Album;
import com.sparta.actualpractice.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByPartyOrderByCreatedAtDesc(Party party);
}

