package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.AlbumRequestDto;
import com.sparta.actualpractice.dto.response.AlbumListResponseDto;
import com.sparta.actualpractice.dto.response.AlbumResponseDto;
import com.sparta.actualpractice.dto.response.CommentResponseDto;
import com.sparta.actualpractice.entity.*;
import com.sparta.actualpractice.repository.AlbumRepository;
import com.sparta.actualpractice.repository.CommentRepository;
import com.sparta.actualpractice.repository.MemberPartyRepository;
import com.sparta.actualpractice.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    @Value("${cloud.aws.s3.bucket}")
    private String dir;

    private final AlbumRepository albumRepository;
    private final PartyRepository partyRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final S3UploadService s3UploadService;

    private final CommentRepository commentRepository;

    public ResponseEntity<?> createAlbum(Long partyId, AlbumRequestDto albumRequestDto, Member member) throws IOException {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        checkAuthority(member, party);

        Album album = Album.builder()
                .content(albumRequestDto.getContent())
                .imageUrl(s3UploadService.upload(albumRequestDto.getImageUrl(),dir))
                .member(member)
                .place(albumRequestDto.getPlace())
                .party(party)
                .build();

        albumRepository.save(album);

        return new ResponseEntity<>("사진이 등록되었습니다.", HttpStatus.OK);
    }


    public ResponseEntity<?> getAlbumList(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        checkAuthority(member, party);

        List<Album> albumList = albumRepository.findAllByPartyOrderByCreatedAtDesc(party);
        List<AlbumListResponseDto> albumListResponseDtoList = new ArrayList<>();

        for(Album album : albumList) {
            albumListResponseDtoList.add(new AlbumListResponseDto(album));
        }

        return new ResponseEntity<>(albumListResponseDtoList, HttpStatus.OK);
    }


    public ResponseEntity<?> getAlbumDetail(Long albumId) {

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NullPointerException("해당 사진이 존재하지 않습니다."));

        List<Comment> commentList = commentRepository.findAllByAlbumOrderByCreatedAtDesc(album);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }

        return new ResponseEntity<>(AlbumResponseDto.builder()
                .content(album.getContent())
                .writer(album.getMember().getName())
                .place(album.getPlace())
                .profileImageUrl(album.getMember().getImageUrl())
                .imageUrl(album.getImageUrl())
                .commentList(commentResponseDtoList)
                .beforeTime(Time.calculateTime(album))
                .build(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> updateAlbum(Long albumId, AlbumRequestDto albumRequestDto, Member member) {

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NullPointerException("해당 사진이 존재하지 않습니다."));

        if(validateMember(member, album))
            throw new IllegalArgumentException("앨범 작성자와 현재 사용자가 일치하지 않습니다.");


        album.update(albumRequestDto);

        return new ResponseEntity<>("앨범 정보가 수정되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteAlbum(Long albumId, Member member) {

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NullPointerException("해당 사진이 존재하지 않습니다."));

        if(validateMember(member, album))
            throw new IllegalArgumentException("앨범 작성자와 현재 사용자가 일치하지 않습니다.");

        albumRepository.delete(album);

        return new ResponseEntity<>("앨범 정보가 삭제되었습니다.", HttpStatus.OK);
    }

    public boolean validateMember(Member member, Album album) {

        return !member.getEmail().equals(album.getMember().getEmail());
    }


    public void checkAuthority(Member member, Party party) {

        if( !memberPartyRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("현재 사용자는 앨범에 접근 할 권한이 없습니다.");
    }

}