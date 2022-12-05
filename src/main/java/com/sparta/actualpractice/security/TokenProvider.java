package com.sparta.actualpractice.security;

import com.sparta.actualpractice.dto.TokenDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    private static final String BEARER_TYPE = "bearer";
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 1;            // 30분

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 30;            // 30분

    //    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 1;  // 7일
        private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 30 ;  // 7일


    private final MemberRepository memberRepository;
    private final Key key;

    @Autowired
    public TokenProvider(@Value("${jwt.secret.key}") String secretKey, MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Member member) {
        // 권한들 가져오기

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())       // payload "sub": "Email"
                .setAudience(member.getName())             // payload "aud": "name"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        String email = claims.getSubject();
        Member member = memberRepository.findByEmail( email )
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다 : " + email ));

        MemberDetailsImpl memberDetails = new MemberDetailsImpl(member);
        System.out.println("memberDetails.getMember().getEmail() = " + memberDetails.getMember().getEmail());

        return new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {

        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String decodedEmail(String token) {

        String jwtToken = token.substring(7);

        if(!validateToken(jwtToken))
            throw new IllegalArgumentException("토큰에 문제 있어!!");

        Claims claims = parseClaims(jwtToken);

        return claims.getSubject();
    }

    public String decodeMemberEmail(String token) {

        String jwtToken = token.substring(7);

        Claims claims = parseClaims(jwtToken);

        return claims.getSubject();
    }

    public Long decodeRefreshTokenExpiration(String token) {

        Claims claims = parseClaims(token);

        return claims.getExpiration().getTime();
    }

}
