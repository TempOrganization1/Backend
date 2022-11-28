package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.MemberParty;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {

    List<MemberParty> findAllByMember(Member member);

    Optional<MemberParty> findByMemberAndParty(Member member, Party party);

    boolean existsByMemberAndParty(Member member, Party party);

    boolean existsByMember_EmailAndParty(String email, Party party);

    void deleteByPartyAndMember(Party party, Member member);

    List<MemberParty> findAllByParty(Party party);
}
