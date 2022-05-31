package com.example.demo.actions.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// アカウント情報について画面の入力値・出力値を扱うViewモデル

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountView {
    // id
    private Integer id;

    // アカウント名
    private String name;

    // メールアドレス
    private String email;

    // パスワード
    private String password;

    // 管理者権限
    private Integer adminFlag;

    // 作成日
    private String createdAt;

    // 更新日
    private String updatedAt;

    // 削除日
    private String deletedAt;
}
