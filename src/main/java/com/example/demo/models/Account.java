package com.example.demo.models;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// アカウントデータのDTOモデル

@Table(name="account")

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Account implements UserDetails{


    // id
    @Id
    @Column(name = "acc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // メールアドレス
    @Column(name="acc_email", length=255, nullable=false)
    private String email;

    // パスワード
    @Column(name="acc_pass", length=255, nullable=false)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
