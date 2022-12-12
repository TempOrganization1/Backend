package com.sparta.actualpractice.party;

import com.sparta.actualpractice.party.Admin;
import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByMemberAndParty(Member member, Party party);
}
