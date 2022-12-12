package com.sparta.actualpractice.memberParty;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.memberParty.MemberParty;
import com.sparta.actualpractice.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {

    List<MemberParty> findAllByMember(Member member);

    Optional<MemberParty> findByMemberAndParty(Member member, Party party);

    boolean existsByMemberAndParty(Member member, Party party);

    void deleteByPartyAndMember(Party party, Member member);

    List<MemberParty> findAllByParty(Party party);

}
