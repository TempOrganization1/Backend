package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.MemberParty;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {


    List<MemberParty> findAllByMember(Member member);
    boolean existsByMemberAndParty(Member member, Party party);
    void deleteByPartyAndMember(Party party, Member member);
}
