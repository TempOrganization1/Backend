package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.AlbumRequestDto;
import com.sparta.actualpractice.dto.response.*;
import com.sparta.actualpractice.entity.*;
import com.sparta.actualpractice.repository.*;
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
    private final ImageRepository imageRepository;

    public ResponseEntity<?> createAlbum(Long partyId, AlbumRequestDto albumRequestDto, Member member) throws IOException {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        checkAuthority(member, party);

        Album album = new Album(albumRequestDto, member, party);

        albumRepository.save(album);

        List<Image> imageList = new ArrayList<>();

        for(int i = 0; i < albumRequestDto.getImageList().size(); i++) {

            Image image = new Image(album, s3UploadService.upload(albumRequestDto.getImageList().get(i), dir));

            imageRepository.save(image);

            imageList.add(image);
        }

        album.updateImageList(imageList);

        return new ResponseEntity<>(new AlbumCreationResponseDto(album), HttpStatus.OK);
    }


    public ResponseEntity<?> getAlbumList(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        checkAuthority(member, party);

        List<Album> albumList = albumRepository.findAllByPartyOrderByCreatedAtDesc(party);
        List<AlbumListResponseDto> albumListResponseDtoList = new ArrayList<>();

        for(Album album : albumList) {

            List<Image> imageList = imageRepository.findAllByAlbumOrderById(album);
            List<ImageResponseDto> imageResponseDtoList = new ArrayList<>();

            Image image = imageRepository.findTopByAlbumOrderById(album);

            albumListResponseDtoList.add(new AlbumListResponseDto(album, image));
        }

        return new ResponseEntity<>(albumListResponseDtoList, HttpStatus.OK);
    }


    public ResponseEntity<?> getAlbumDetail(Long albumId) {

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NullPointerException("해당 사진이 존재하지 않습니다."));

        List<Image> imageList = imageRepository.findAllByAlbumOrderById(album);
        List<ImageResponseDto> imageResponseDtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAllByAlbumOrderByCreatedAtDesc(album);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }

        for(Image image : imageList) {
            imageResponseDtoList.add(new ImageResponseDto(image));
        }

        return new ResponseEntity<>(AlbumResponseDto.builder()
                .content(album.getContent())
                .writer(album.getMember().getName())
                .place(album.getPlace())
                .profileImageUrl(album.getMember().getImageUrl())
                .imageList(imageResponseDtoList)
                .commentList(commentResponseDtoList)
                .beforeTime(Time.calculateTime(album))
                .memberEmail(album.getMember().getEmail())
                .build(), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> updateAlbum(Long albumId, AlbumRequestDto albumRequestDto, Member member) {

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NullPointerException("해당 사진이 존재하지 않습니다."));

        if(validateMember(member, album))
            throw new IllegalArgumentException("앨범 작성자와 현재 사용자가 일치하지 않습니다.");


        album.updateContent(albumRequestDto);

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