package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Admin;
import com.sparta.actualpractice.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    boolean existsByCode(String code);

    Optional<Party> findByCode(String code);
    Optional<Party> findByAdmin(Admin admin);

}
