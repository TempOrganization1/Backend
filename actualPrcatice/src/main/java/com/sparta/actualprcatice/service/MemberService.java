package com.sparta.actualprcatice.service;

import com.sparta.actualproject.dto.TokenDto;
import com.sparta.actualproject.dto.request.MemberReqeustDto;
import com.sparta.actualproject.entity.Member;
import com.sparta.actualproject.entity.RefreshToken;
import com.sparta.actualproject.repository.MemberRepository;
import com.sparta.actualproject.repository.RefreshTokenRepository;
import com.sparta.actualproject.security.JwtFilter;
import com.sparta.actualproject.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

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


    public ResponseEntity<?> login(MemberReqeustDto memberReqeustDto, HttpServletResponse response) {

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

        response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());

        return new ResponseEntity<>("로그인에 성공했습니다.",HttpStatus.OK);
    }
}
