package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.PasswordNewCreate;

@Repository
public interface PasswordNewCreateRepository extends JpaRepository<PasswordNewCreate,Integer> {

    // アカウントidを元に削除されていないパスワード新規作成データを取得
    @Query(value="SELECT pnc FROM PasswordNewCreate AS pnc , Account AS ac, OnetimePassword AS otp WHERE otp.deletedAt IS NULL AND pnc.ac.id = :id")
    PasswordNewCreate findByDeletedAtIsNullANDIdIs(Integer id);

    // トークンを元に削除されていないパスワード新規作成データを取得
    @Query(value="SELECT pnc FROM PasswordNewCreate AS pnc , OnetimePassword AS otp WHERE pnc.otp = otp.id AND otp.deletedAt IS NULL AND otp.token = :token")
    PasswordNewCreate findByDeletedAtIsNullANDTokenIs(String token);

}
