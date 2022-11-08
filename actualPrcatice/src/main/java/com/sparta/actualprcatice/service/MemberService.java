package com.sparta.actualprcatice.service;


import com.example.consolelog.dto.TokenDto;
import com.example.consolelog.dto.requestDto.MemberReqeustDto;
import com.example.consolelog.dto.requestDto.TokenRequestDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.Member;
import com.example.consolelog.entity.RefreshToken;
import com.example.consolelog.repository.MemberRepository;
import com.example.consolelog.repository.RefreshTokenRepository;
import com.example.consolelog.security.JwtFilter;
import com.example.consolelog.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseDto<?> signup(MemberReqeustDto memberReqeustDto) {

        if (memberRepository.existsByName(memberReqeustDto.getName()) && memberRepository.existsByNickname(memberReqeustDto.getNickname()))
            throw new RuntimeException("이미 존재하는 Name 또는 Nickname 입니다.");

        Member member = new Member(memberReqeustDto, passwordEncoder.encode(memberReqeustDto.getPassword()));

        memberRepository.save(member);

        return ResponseDto.success("회원가입 성공했습니다.");
    }

    public ResponseDto<?> login(MemberReqeustDto memberRequestDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Member member = memberRepository.findByName(authentication.getName()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));


        if(!passwordEncoder.matches(memberRequestDto.getPassword(), member.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // 커스텀 예외 처리 예정

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        response.setHeader(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());
        return ResponseDto.success("로그인에 성공했습니다.");

    }

    public ResponseDto<?> checkName(String name) {

        if (memberRepository.existsByName(name)) {
            throw new RuntimeException("중복된 아이디 입니다.");
        }


        return ResponseDto.success(true);
    }

    public ResponseDto<?> checkNickname(String nickname) {

        if (memberRepository.existsByNickname(nickname))
            throw new RuntimeException("중복된 닉네임 입니다.");

        return ResponseDto.success(true);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {

        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh 토큰이 유효하지 않습니다.");
        }
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }
}
