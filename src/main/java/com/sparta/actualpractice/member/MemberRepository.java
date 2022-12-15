package com.sparta.actualpractice.member;


import com.sparta.actualpractice.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    boolean existsByEmail(String email);

    Optional<Member> findByKakaoId(String kakaoId);

    Optional<Member> findByGoogleId(String googleId);
}
