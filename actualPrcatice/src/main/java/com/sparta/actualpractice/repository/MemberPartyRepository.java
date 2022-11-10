package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.MemberParty;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {


    List<MemberParty> findAllByMember_Id(Long memberId);

    MemberParty findByMemberAndParty(Member member, Party party);

    boolean existsByPartyAndMember(Party party, Member member);

    void deleteByPartyAndMember(Party party, Member member);
}
