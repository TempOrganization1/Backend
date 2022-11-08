package com.sparta.actualprcatice.repository;

import com.sparta.actualprcatice.entity.Member;
import com.sparta.actualprcatice.entity.MemberParty;
import com.sparta.actualprcatice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {


    List<MemberParty> findAllByMember_Id(Long memberId);

    MemberParty findByMemberAndParty(Member member, Party party);
}
