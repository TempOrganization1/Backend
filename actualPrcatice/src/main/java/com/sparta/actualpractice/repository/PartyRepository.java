package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Participant;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {

}
