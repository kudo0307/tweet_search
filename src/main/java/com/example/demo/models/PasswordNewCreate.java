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
// パスワード新規作成テーブルのDTOモデル
@Table(name="password_new_create")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PasswordNewCreate {


    // id
    @Id
    @Column(name = "pnc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="pnc_acc_id",nullable=false)
    private Account ac;

    // ワンタイムパスワードテーブル
    @OneToOne
    @JoinColumn(name="pnc_otp_id",nullable=false)
    private OnetimePassword otp;
}
