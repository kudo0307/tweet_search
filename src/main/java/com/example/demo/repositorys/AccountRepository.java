package com.example.demo.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    // 削除されていないアカウントデータを取得
    @Query(value="SELECT a FROM Account AS a WHERE a.deletedAt IS NULL ORDER BY a.id DESC")
    List<Account> findAllByDeletedAtIsNullOrderDescById();

    // idを元に削除されていないアカウントデータを取得
    @Query(value="SELECT a FROM Account AS a WHERE a.deletedAt IS NULL AND a.id = :id")
    Account findByDeletedAtIsNullById(@Param("id") Integer id);

    // メールアドレスを元に削除されていないアカウントデータを取得
    @Query(value="SELECT a FROM Account AS a WHERE a.deletedAt IS NULL AND a.email = :email")
    Account findByDeletedAtIsNullANDEmailIs(String email);
}
