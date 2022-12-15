package com.sparta.actualpractice.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.actualpractice.util.OauthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String googleRedirectUrl;

    private final MemberRepository memberRepository;
    private final OauthUtil oauthUtil;

    public ResponseEntity<?> googleLogin(String code) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        OAuth2memberInfoDto googleUserInfo = getGoogleUserInfo(accessToken);

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Member googleUser = registerGoogleUserIfNeeded(googleUserInfo);

        if (googleUser == null)
            return new ResponseEntity<>("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);

        // 3.5 기본 그룹 가입하기
        oauthUtil.basicParty(googleUser);

        // 4. 강제 로그인 처리
        oauthUtil.forceLogin(googleUser);

        // 5. 토큰 생성
        TokenDto tokenDto = oauthUtil.generateTokenDto(googleUser);

        // 6. 톸큰 해더에 담기
        HttpHeaders headers = oauthUtil.setHeaders(tokenDto);

        // 6.5 카카오 "엑세스 토큰" 레디스 저장
        oauthUtil.OauthAceessTokenToRedisSave(accessToken, googleUser, "google");

        return new ResponseEntity<>("구글 로그인에 성공하였습니다.", headers, HttpStatus.OK);
    }

    private String getAccessToken(String code) throws JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", googleClientId);
        body.add("redirect_uri", googleRedirectUrl);
        body.add("client_secret", googleClientSecret);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    private OAuth2memberInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST,
                googleUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String id = jsonNode.get("sub").asText();

        String nickname = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String imageUrl = jsonNode.get("picture").asText();

        return new OAuth2memberInfoDto(id, nickname, email, imageUrl);
    }

    private Member registerGoogleUserIfNeeded(OAuth2memberInfoDto googleUserInfo) {

        Member googleMember = memberRepository.findByGoogleId(googleUserInfo.getId()).orElse(null);

        if (googleMember == null && !memberRepository.existsByEmail(googleUserInfo.getEmail())) {
            // 회원가입
            googleMember = Member.builder()
                    .googleId(googleUserInfo.getId())
                    .email(googleUserInfo.getEmail())
                    .name(googleUserInfo.getNickname())
                    .imageUrl(googleUserInfo.getImageUrl())
                    .build();

            memberRepository.save(googleMember);
        }
        return googleMember;
    }
}
