package com.sparta.actualprcatice.repository;

import com.sparta.actualprcatice.entity.Member;
import com.sparta.actualprcatice.entity.MemberParty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {


    List<MemberParty> findAllByMember_Id(Long memberId);
}
