package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.EmailUpdate;

@Repository
public interface EmailUpdateRepository extends JpaRepository<EmailUpdate,Integer> {
    @Query(value="SELECT emu FROM EmailUpdate AS emu WHERE emu.otp.deletedAt IS NULL AND emu.ac.id = :id")
    EmailUpdate findByDeletedAtIsNullANDAccIdIs(Integer id);

    @Query(value="SELECT emu FROM EmailUpdate AS emu WHERE  emu.otp.deletedAt IS NULL AND otp.token = :token")
    EmailUpdate findByDeletedAtIsNullANDTokenIs(String token);

}
