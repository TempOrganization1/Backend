package com.sparta.actualpractice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.actualpractice.dto.TokenDto;
import com.sparta.actualpractice.dto.response.OAuth2memberInfoDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.repository.MemberRepository;
import com.sparta.actualpractice.util.OauthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.client.id}")
    private String kakaoClinetId;
    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUrl;
    private final MemberRepository memberRepository;
    private final OauthUtil oauthUtil;

    public ResponseEntity<?> kakaoLogin(String code) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        OAuth2memberInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 3.5 기본 그룹 가입하기
        oauthUtil.basicParty(kakaoUser);

        // 4. 강제 로그인 처리
        oauthUtil.forceLogin(kakaoUser);

        // 5. 토큰 생성.
        TokenDto tokenDto = oauthUtil.generateTokenDto(kakaoUser);

        // 6. 토큰 해더에 담기.
        HttpHeaders headers = oauthUtil.setHeaders(tokenDto);

        return new ResponseEntity<>("카카오 로그인에 성공했습니다.", headers, HttpStatus.OK);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClinetId);
        body.add("redirect_uri", kakaoRedirectUrl);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    private OAuth2memberInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String imageUrl = jsonNode.get("kakao_account")
                .get("profile")
                .get("profile_image_url").asText();

        return new OAuth2memberInfoDto(id, nickname, email, imageUrl);
    }

    @Transactional
    public Member registerKakaoUserIfNeeded(OAuth2memberInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Member kakaoMember = memberRepository.findByKakaoId(kakaoUserInfo.getId()).orElse(null);

        if (kakaoMember == null) {
            // 회원가입
            kakaoMember = Member.builder()
                    .kakaoId(kakaoUserInfo.getId())
                    .email(kakaoUserInfo.getEmail())
                    .name(kakaoUserInfo.getNickname())
                    .imageUrl(kakaoUserInfo.getImageUrl())
                    .build();

            memberRepository.save(kakaoMember);
        }

        return kakaoMember;
    }
}
