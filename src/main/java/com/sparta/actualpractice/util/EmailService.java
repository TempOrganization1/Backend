package com.sparta.actualpractice.util;

import com.sparta.actualpractice.member.MemberController;
import com.sparta.actualpractice.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final String ePw = createKey();
    private final MemberRepository memberRepository;

    @Value("${naver.username}")
    private String userName;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        log.info("대상 : " + to);
        log.info("인증 번호 : " + ePw);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("WEF 회원가입 인증 코드 : ");

        String msg = "";

        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(userName, "위프"));

        return message;
    }

    public static String createKey() {

        StringBuilder key = new StringBuilder();
        Random ran = new Random();

        for (int i = 0; i < 6; i++) {
            key.append(ran.nextInt(10));
        }

        return key.toString();
    }

    public ResponseEntity<?> sendSimpleMessage(String to) throws Exception {

        if (memberRepository.existsByEmail(to))
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");

        MimeMessage message = createMessage(to);

        try {
            javaMailSender.send(message);
        }catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        redisUtil.setDataExpire(ePw, to, 60 * 5L);

        return new ResponseEntity<>("인증 메일을 전송했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> authenticateEmail(String to) {

        if (redisUtil.getData(to).equals())
    }
}
