package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.TokenDto;
import com.sparta.actualpractice.dto.request.MemberInfoRequestDto;
import com.sparta.actualpractice.dto.request.MemberReqeustDto;
import com.sparta.actualpractice.dto.response.MemberResponseDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.MemberParty;
import com.sparta.actualpractice.entity.Party;
import com.sparta.actualpractice.entity.RefreshToken;
import com.sparta.actualpractice.repository.MemberPartyRepository;
import com.sparta.actualpractice.repository.MemberRepository;
import com.sparta.actualpractice.repository.PartyRepository;
import com.sparta.actualpractice.repository.RefreshTokenRepository;
import com.sparta.actualpractice.security.JwtFilter;
import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${cloud.aws.s3.bucket}")
    private String dir;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final S3UploadService s3UploadService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PartyRepository partyRepository;

    private final MemberPartyRepository memberPartyRepository;

    public ResponseEntity<?> signup(MemberReqeustDto memberReqeustDto) {

        if(memberRepository.existsByEmail(memberReqeustDto.getEmail())){
            return new ResponseEntity<>("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        Member member = new Member(memberReqeustDto, passwordEncoder.encode(memberReqeustDto.getPassword()));

        memberRepository.save(member);


        // 기본 그룹 가입

        if(member.getId() == 1L) {

            String name = "위프";
            String introduction = "모두와 함께 다양한 기능들을 경험해보세요!\n" +
                    "\n" +
                    "\uD83D\uDE46\uD83C\uDFFB\u200D♀️ 새로운 그룹을 만들면 초대 코드를 통해 친구들과 소중한 추억을 공유하실 수 있습니다 !";

            Party party = new Party(name, introduction);

            partyRepository.save(party);
        }

        Party party = partyRepository.findById(1L).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        MemberParty memberParty = new MemberParty(member, party);

        memberPartyRepository.save(memberParty);


        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }


    public ResponseEntity<?> login(MemberReqeustDto memberReqeustDto) {

        UsernamePasswordAuthenticationToken authenticationToken = memberReqeustDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(memberReqeustDto.getPassword(), member.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // 커스텀 예외 처리 예정

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        headers.set("Refresh-Token", tokenDto.getRefreshToken());

        return new ResponseEntity<>("로그인에 성공했습니다.", headers, HttpStatus.OK);
    }

    public ResponseEntity<?> checkEmail(String email) {

        if (memberRepository.existsByEmail(email))
            return new ResponseEntity<>("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);  // 추후에 커스텀 예외 처리 예정

        return new ResponseEntity<>("사용 가능한 이메일입니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getMyInfo(Member member) {

        Member member1 = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        return new ResponseEntity<>(new MemberResponseDto(member1), HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> updateMyInfo(MemberInfoRequestDto memberInfoRequestDto, Member member) throws IOException {

        Member member1 = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        String imageUrl = s3UploadService.upload(memberInfoRequestDto.getProfileImageUrl(), dir);

        member1.updateImage(imageUrl);

        return new ResponseEntity<>(new MemberResponseDto(member1), HttpStatus.OK);
    }


}
