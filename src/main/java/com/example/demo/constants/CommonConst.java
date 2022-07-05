package com.example.demo.constants;

// 定数クラス
public interface CommonConst {

    // tweet検索時の取得件数
    String TWEET_MAX_RESULT = "10";

    // 二重送信防止トークン
    // 生成文字数
    int PROTECTION_TOKEN_INT = 20;
    // 文字列
    String PROTECTION_TOKEN_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
}
