package com.example.demo.repositorys;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.OnetimePassword;

@Repository
public interface OnetimePasswordRepository extends JpaRepository<OnetimePassword,Integer> {

    OnetimePassword findByTokenIsAndTokenAtBefore(String token,LocalDateTime now);
}
