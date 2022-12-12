package com.sparta.actualpractice.party;

import com.sparta.actualpractice.party.Admin;
import com.sparta.actualpractice.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    boolean existsByCode(String code);

    Optional<Party> findByCode(String code);
    Optional<Party> findByAdmin(Admin admin);

}
