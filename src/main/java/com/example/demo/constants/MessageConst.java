package com.example.demo.constants;

// メッセージに関する文字を定義するファイル
public interface MessageConst {

    // アカウント
    String ACCOUNT_ALREADY_EXISTS = "入力いただいたメールアドレスは既に登録されています";
    String ACCOUNT_NOT_EXISTS = "アカウントが登録されていません";

    // バリデーションエラーメッセージ

    // メールアドレス
    String VALID_EMAIL = "メールアドレスを正しく入力してください";
    String VALID_NAME = "アカウント名を正しく入力してください";

    // パスワード
    String VALID_PASSWORD_NOT_BLANK = "パスワードは必須です";
    String VALID_PASSWORD_MIN = "パスワードは"+JpaConst.PASSWORD_MIN + "文字以上で入力してください";
    String VALID_PASSWORD_CONFIRM = "パスワードと再入力パスワードが一致しません";


    // メール
    String MAIL_URL_PATH = "http://localhost:8080";
    // from
    String MAIL_FROM = "tweetsearchinfo@gmail.com";
    // cc
    String MAIL_CC = "tweetsearchinfo@gmail.com";

    // アカウント新規作成
    String ACCOUNT_NEW_CREATE_MAIL_SUBJECT = "アカウント新規作成手続き"; // 件名
    String ACCOUNT_NEW_CREATE_MAIL_TEXT =
            "下記にて本登録のお手続きをお願い致します。\n\n"
            + "URL : " + MAIL_URL_PATH + "/accountNewCreate/create?token=${token}"; // 本文

    // パスワード新規作成
    String PASSWORD_NEW_CREATE_MAIL_SUBJECT = "パスワード再発行手続き"; // 件名
    String PASSWORD_NEW_CREATE_MAIL_TEXT =
            "下記にてパスワードの再発行をお願い致します。\n\n"
            + "URL : " + MAIL_URL_PATH + "/passwordNewCreate/create?token=${token}"; // 本文

}
