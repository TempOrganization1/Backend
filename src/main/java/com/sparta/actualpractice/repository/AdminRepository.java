package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Admin;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByMemberAndParty(Member member, Party party);
}
