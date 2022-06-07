package com.example.demo.constants;

// DB関連の定数クラス
public interface JpaConst {

    // アカウント

    int PASSWORD_MIN = 8; // パスワードの最小文字数


    // 管理者権限
    int ROLE_ADMIN = 1; // 管理者
    int ROLE_GENERAL = 0; // 一般

    // ワンタイムパスワードテーブル
    int OTP_TOKENAT_MINUTE = 15; // トークン期限作成時加算する分

    // ワンタイムパスワード生成用文字数
    int ONETIME_PASS_INT = 10;
    // ワンタイムパスワード生成用文字列
    String ONETIME_PASS_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
}
