package com.sparta.actualpractice.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class MemberControllerTest {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberControllerTest(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {

        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    @Rollback
    @DisplayName("회원가입 테스트")
    void signup() {

        Member member = Member.builder()
                .email("1234@gmail.com")
                .name("테스트")
                .password(passwordEncoder.encode("asdf1234"))
                .build();

        Assertions.assertThat(member.getEmail()).isEqualTo("1234@gmail.com");
        Assertions.assertThat(member.getName()).isEqualTo("테스트");

        memberRepository.save(member);
    }

    @Test
    void checkEmail() {
    }
}