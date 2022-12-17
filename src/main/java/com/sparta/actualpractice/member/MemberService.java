package com.sparta.actualpractice.member;

import com.sparta.actualpractice.chat.ChatRoomRepository;
import com.sparta.actualpractice.party.PartyRepository;
import com.sparta.actualpractice.security.TokenProvider;
import com.sparta.actualpractice.util.EmailService;
import com.sparta.actualpractice.util.S3UploadService;
import com.sparta.actualpractice.util.OauthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${cloud.aws.s3.bucket}")
    private String dir;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final OauthUtil oauthUtil;
    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final EmailService emailService;
    public ResponseEntity<?> signup(MemberRequestDto memberRequestDto) {

        if(memberRepository.existsByEmail(memberRequestDto.getEmail()))
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .name(memberRequestDto.getName())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .build();


        memberRepository.save(member);

        oauthUtil.basicParty(member);

        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> login(MemberRequestDto memberRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(memberRequestDto.getPassword(), member.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        TokenDto tokenDto = oauthUtil.generateTokenDto(member);

        HttpHeaders headers = oauthUtil.setHeaders(tokenDto);

        return new ResponseEntity<>("로그인에 성공했습니다.", headers, HttpStatus.OK);
    }

    public ResponseEntity<?> sendEmail(String email) {


        return new ResponseEntity<>("인증 메일을 발송했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getMyInfo(Member member) {

        Member member1 = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        return new ResponseEntity<>(new MemberResponseDto(member1), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateMyInfo(MemberInfoRequestDto memberInfoRequestDto, Member member) throws IOException {

        Member member1 = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new NullPointerException("해당 사용자를 찾을 수 없습니다."));

        if (memberInfoRequestDto.getProfileImage() != null) {

            String imageUrl = s3UploadService.upload(memberInfoRequestDto.getProfileImage(), dir);

            member1.updateImage(imageUrl);
        }

        member1.updateName(memberInfoRequestDto.getMemberName());

        return new ResponseEntity<>(new MemberResponseDto(member1), HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(TokenRequestDto tokenRequestDto) {

        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken()))
            throw new IllegalArgumentException("RefreshToken에 문제가 있습니다.");

        Member member = memberRepository.findByEmail(tokenProvider.decodeMemberEmail(tokenRequestDto.getAccessToken()))
                .orElseThrow(() -> new NullPointerException("존재 하지 않는 회원입니다."));

        String refreshToken = (String) redisTemplate.opsForValue().get("RefreshToken:" + member.getEmail());

        if (refreshToken == null)
            throw new NullPointerException("Refresh 토큰이 존재하지 않습니다.");

        if (!refreshToken.equals(tokenRequestDto.getRefreshToken()))
            throw new IllegalArgumentException("RefreshToken의 정보가 일치 하지 않습니다.");

        TokenDto tokenDto = oauthUtil.generateTokenDto(member);

        HttpHeaders headers = oauthUtil.setHeaders(tokenDto);

        return new ResponseEntity<>("토큰 재발급에 성공했습니다.", headers, HttpStatus.OK);
    }

    public ResponseEntity<?> delete(Member member) {

        if (member.getKakaoId() != null) {

            String accessToken = (String) redisTemplate.opsForValue().get("kakaoAccessToken:" + member.getEmail());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v1/user/unlink",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
            );
        }

        memberRepository.delete(member);
        redisTemplate.delete("RefreshToken:" + member.getEmail());
        redisTemplate.delete("kakaoAccessToken:" + member.getEmail());

        return new ResponseEntity<>("회원탈퇴가 완료되었습니다.", HttpStatus.OK);
    }
}