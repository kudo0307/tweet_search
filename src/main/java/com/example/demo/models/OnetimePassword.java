package com.example.demo.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ワンタイムパスワードテーブルのDTOモデル

@Table(name="onetime_password")

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OnetimePassword{

    // グループ作成
    public static interface CreateData{}

    // id
    @Id
    @Column(name = "otp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // トークン
    @NotBlank(groups= { CreateData.class })
    @Column(name="otp_token",length = 10, nullable = false,unique=true)
    private String token;

    // トークン期限
    @Column(name="otp_token_at", nullable=false)
    private LocalDateTime tokenAt;

    // 削除日
    @Column(name="otp_deleted_at")
    private LocalDateTime deletedAt;

}
