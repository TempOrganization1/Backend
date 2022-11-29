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

        String accessToken = getAccessToken(code);

        OAuth2memberInfoDto googleUserInfo = getGoogleUserInfo(accessToken);

        Member googleUser = registerGoogleUserIfNeeded(googleUserInfo);

        oauthUtil.forceLogin(googleUser);

        TokenDto tokenDto = oauthUtil.generateTokenDto(googleUser);

        HttpHeaders headers = oauthUtil.setHeaders(tokenDto);

        return new ResponseEntity<>("구글 로그인에 성공하였습니다.",  HttpStatus.OK);
    }


    private String getAccessToken(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("User-Agent","PostmanRuntime/7.15.0");

        System.out.println(code);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("redirect_uri", googleRedirectUrl);
        body.add("code", code);


        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    private OAuth2memberInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

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

        System.out.println(jsonNode);

        Long id = jsonNode.get("sub").asLong();
        String nickname = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String imageUrl = jsonNode.get("picture").asText();

        return new OAuth2memberInfoDto(id, nickname, email, imageUrl);
    }

    private Member registerGoogleUserIfNeeded(OAuth2memberInfoDto googleUserInfo) {

        Member googleMember = memberRepository.findByKakaoId(googleUserInfo.getId()).orElse(null);


        if (googleMember == null) {
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
