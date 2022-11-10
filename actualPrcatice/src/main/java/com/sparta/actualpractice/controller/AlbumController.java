package com.sparta.actualpractice.controller;


import com.sparta.actualpractice.dto.request.AlbumRequestDto;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;


    @PostMapping("/{party_id}/album")
    public ResponseEntity<?> createAlbum(@PathVariable(name = "party_id") Long partyId, @ModelAttribute AlbumRequestDto albumRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {

        return albumService.createAlbum(partyId, albumRequestDto, memberDetails.getMember());
    }

    @GetMapping("/{party_id}/album")
    public ResponseEntity<?> getAlbumList(@PathVariable(name = "party_id") Long partyId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return albumService.getAlbumList(partyId, memberDetails.getMember());
    }

    @GetMapping("/album/{album_id}")
    public ResponseEntity<?> getAlbumDetail(@PathVariable(name = "album_id") Long albumId) {

        return albumService.getAlbumDetail(albumId);
    }
}