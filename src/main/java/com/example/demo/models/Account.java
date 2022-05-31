package com.example.demo.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// アカウントデータのDTOモデル

@Table(name="account")
@NamedQueries({
    @NamedQuery(
            name = "account.getAll",
            query = "SELECT a FROM Account AS a WHERE a.acc_deleted_at IS NOT NULL"
            )
})

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    // id
    @Id
    @Column(name = "acc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // アカウント名
    @Column(name="acc_name",length = 50, nullable = false)
    private String name;

    // メールアドレス
    @Column(name="acc_email", length=255, nullable=false)
    private String email;

    // パスワード
    @Column(name="acc_email", length=255, nullable=false)
    private String password;

    // 管理者権限
    @Column(name="acc_admin_flag", nullable = false)
    private Integer adminFlag;

    // 作成日
    @Column(name="acc_created_at", nullable=false)
    private LocalDateTime createdAt;

    // 更新日
    @Column(name="acc_updated_at", nullable=false)
    private LocalDateTime updatedAt;

    // 削除日
    @Column(name="acc_deleted_at")
    private LocalDateTime deletedAt;

}
