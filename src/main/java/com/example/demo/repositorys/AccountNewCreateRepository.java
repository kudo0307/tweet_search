package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AccountNewCreate;

@Repository
public interface AccountNewCreateRepository extends JpaRepository<AccountNewCreate,Integer> {
    @Query(value="SELECT anc FROM AccountNewCreate AS anc , OnetimePassword AS otp WHERE anc.otp = otp.id AND otp.deletedAt IS NULL AND anc.email = :email")
    AccountNewCreate findByDeletedAtIsNullANDEmailIs(String email);

    @Query(value="SELECT anc FROM AccountNewCreate AS anc , OnetimePassword AS otp WHERE anc.otp = otp.id AND otp.deletedAt IS NULL AND otp.token = :token")
    AccountNewCreate findByDeletedAtIsNullANDTokenIs(String token);

}
