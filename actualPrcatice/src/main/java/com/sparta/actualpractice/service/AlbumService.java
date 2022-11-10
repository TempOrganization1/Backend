package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.AlbumRequestDto;
import com.sparta.actualpractice.dto.response.AlbumListResponseDto;
import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.MemberParty;
import com.sparta.actualpractice.entity.Party;
import com.sparta.actualpractice.repository.AlbumRepository;
import com.sparta.actualpractice.repository.MemberPartyRepository;
import com.sparta.actualpractice.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    @Value("i3e2-test-bucket")
    private String dir;

    private final AlbumRepository albumRepository;
    private final PartyRepository partyRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final S3UploadService s3UploadService;


    public ResponseEntity<?> createAlbum(Long partyId, AlbumRequestDto albumRequestDto, Member member) throws IOException {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if( !memberPartyRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("현재 사용자는 앨범에 접근 할 권한이 없습니다.");


        //Album album = new Album(albumRequestDto, member, party);
        Album album = Album.builder()
                .content(albumRequestDto.getContent())
                .imageUrl(s3UploadService.upload(albumRequestDto.getImageUrl(),dir))
                .member(member)
                .party(party)
                .build();

        albumRepository.save(album);

        return new ResponseEntity<>("사진이 등록되었습니다.", HttpStatus.OK);
    }


    public ResponseEntity<?> getAlbumList(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if( !memberPartyRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("현재 사용자는 앨범에 접근 할 권한이 없습니다.");

        List<Album> albumList = albumRepository.findAllByPartyOrderByCreatedAtDesc(party);
        List<AlbumListResponseDto> albumListResponseDtoList = new ArrayList<>();

        for(Album album : albumList) {
            albumListResponseDtoList.add(new AlbumListResponseDto(album));
        }

        return new ResponseEntity<>(albumListResponseDtoList, HttpStatus.OK);
    }



    public boolean validateMember(Member member, Album album) {

        return !member.getEmail().equals(album.getMember().getEmail());
    }
}