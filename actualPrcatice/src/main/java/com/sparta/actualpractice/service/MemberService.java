package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.TokenDto;
import com.sparta.actualpractice.dto.request.MemberReqeustDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.RefreshToken;
import com.sparta.actualpractice.repository.MemberRepository;
import com.sparta.actualpractice.repository.RefreshTokenRepository;
import com.sparta.actualpractice.security.JwtFilter;
import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<?> signup(MemberReqeustDto memberReqeustDto) {

        if(memberRepository.existsByEmail(memberReqeustDto.getEmail())){
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        Member member = new Member(memberReqeustDto, passwordEncoder.encode(memberReqeustDto.getPassword()));

        memberRepository.save(member);

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

        return new ResponseEntity<>("로그인에 성공했습니다.",headers, HttpStatus.OK);
    }
}
