package com.sparta.actualpractice.util;

import com.sparta.actualpractice.member.TokenDto;
import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.memberParty.MemberParty;
import com.sparta.actualpractice.party.Party;
import com.sparta.actualpractice.memberParty.MemberPartyRepository;
import com.sparta.actualpractice.party.PartyRepository;
import com.sparta.actualpractice.security.JwtFilter;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OauthUtil {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 ;            // 1일
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final PartyRepository partyRepository;
    private final MemberPartyRepository memberPartyRepository;

    public void forceLogin(Member kakaoUser) {

        UserDetails userDetails = new MemberDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
    public TokenDto generateTokenDto(Member member) {

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        redisTemplate.opsForValue()
                .set("RefreshToken:" + member.getEmail(), tokenDto.getRefreshToken(),
                        tokenProvider.decodeRefreshTokenExpiration(tokenDto.getRefreshToken()) - new Date().getTime(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    public HttpHeaders setHeaders(TokenDto tokenDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        headers.set("Refresh-Token", tokenDto.getRefreshToken());

        return headers;
    }

    public void basicParty(Member member) {

        Party party = partyRepository.findById(1L).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if (!memberPartyRepository.existsByMemberAndParty(member, party)) {

            MemberParty memberParty = new MemberParty(member, party);

            memberPartyRepository.save(memberParty);
        }
    }

    public void OauthAceessTokenToRedisSave(String accessToken, Member OauthUser, String OauthName) {

        redisTemplate.opsForValue()
                .set(OauthName + "AccessToken:" + OauthUser.getEmail(), accessToken,
                        ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }
}
