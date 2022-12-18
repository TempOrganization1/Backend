package com.sparta.actualpractice.party;

import com.sparta.actualpractice.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByMemberAndParty(Member member, Party party);
}
