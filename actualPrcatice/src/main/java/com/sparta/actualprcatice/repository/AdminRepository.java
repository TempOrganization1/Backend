package com.sparta.actualprcatice.repository;

import com.sparta.actualprcatice.entity.Admin;
import com.sparta.actualprcatice.entity.Member;
import com.sparta.actualprcatice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByMemberAndParty(Member member, Party party);
}
