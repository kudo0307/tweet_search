package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// メールアドレス更新テーブルのDTOモデル
@Table(name="email_update")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EmailUpdate {


    // id
    @Id
    @Column(name = "emu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // アカウントテーブル
    @ManyToOne
    @JoinColumn(name="emu_acc_id",nullable=false)
    private Account ac;

    // メールアドレス
    @Column(name="emu_email", length=255, nullable=false)
    private String email;

    // ワンタイムパスワードテーブル
    @OneToOne
    @JoinColumn(name="emu_otp_id",nullable=false)
    private OnetimePassword otp;
}
