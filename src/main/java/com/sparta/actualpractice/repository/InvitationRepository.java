package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Invitation;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByMemberAndParty(Member member, Party party);

    Optional<Invitation> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByMemberAndParty(Member member, Party party);

}
