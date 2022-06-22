package com.example.demo.repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    // 削除されていないアカウントデータ取得
    @Query(value="SELECT ac FROM Account AS ac WHERE ac.deletedAt IS NULL ORDER BY ac.id DESC")
    Page<Account> findAccountData(Pageable pageable);

    // idを元に削除されていないアカウントデータを取得
    @Query(value="SELECT ac FROM Account AS ac WHERE ac.deletedAt IS NULL AND ac.id = :id")
    Account findByDeletedAtIsNullById(@Param("id") Integer id);

    // メールアドレスを元に削除されていないアカウントデータを取得
    @Query(value="SELECT ac FROM Account AS ac WHERE ac.deletedAt IS NULL AND ac.email = :email")
    Account findByDeletedAtIsNullANDEmailIs(String email);

    // ゲストアカウントを取得
    @Query(value="SELECT ac FROM Account AS ac WHERE ac.deletedAt IS NULL AND ac.adminFlag = :flag")
    Account findByDeletedAtIsNullANDFlagIs(Integer flag);
}
