package com.sparta.actualprcatice.repository;

import com.sparta.actualprcatice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
