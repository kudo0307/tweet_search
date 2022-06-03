package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.demo.constants.MessageConst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// アカウント新規作成テーブルのDTOモデル

@Table(name="account_new_create")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AccountNewCreate {


    // id
    @Id
    @Column(name = "anc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message=MessageConst.VALID_EMAIL)
    @Email(message=MessageConst.VALID_EMAIL)
    // メールアドレス
    @Column(name="anc_email", length=255, nullable=false)
    private String email;

    // ワンタイムパスワードテーブルid
    @Column(name="anc_otp_id",nullable=false)
    private Integer otpId;
}
