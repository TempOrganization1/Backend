package com.sparta.actualpractice.util;

import com.sparta.actualpractice.dto.TokenDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.RefreshToken;
import com.sparta.actualpractice.repository.RefreshTokenRepository;
import com.sparta.actualpractice.security.JwtFilter;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthUtil {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    public void forceLogin(Member kakaoUser) {

        UserDetails userDetails = new MemberDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
    public TokenDto generateTokenDto(Member member) {

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(member.getEmail())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    public HttpHeaders setHeaders(TokenDto tokenDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        headers.set("Refresh-Token", tokenDto.getRefreshToken());

        return headers;
    }
}
