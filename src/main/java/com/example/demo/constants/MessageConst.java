package com.example.demo.constants;

// メッセージに関する文字を定義するファイル
public interface MessageConst {

    // ログアウト
    String LOGOUT_SUCCESS = "ログアウトしました";

    // アカウント
    String ACCOUNT_ALREADY_EXISTS = "入力いただいたメールアドレスは既に登録されています";
    String ACCOUNT_NOT_EXISTS = "アカウントが登録されていません";
    String ACCOUNT_DELETE = "アカウントを削除しました";

    // バリデーションエラーメッセージ

    // メールアドレス
    String VALID_EMAIL = "メールアドレスを正しく入力してください";
    String VALID_EMAIL_EXISTS = "メールアドレスが重複しています";
    // アカウント名
    String VALID_NAME = "アカウント名を正しく入力してください";

    // パスワード
    String VALID_PASSWORD_SIZE = "パスワードは"+JpaConst.PASSWORD_MIN + "文字以上"+JpaConst.PASSWORD_MAX+"以内で入力してください";
    String VALID_PASSWORD_CONFIRM = "パスワードと再入力パスワードが一致しません";

    // ログインエラー
    String VALID_LOGIN = "メールアドレスまたはパスワードが間違っています";

    /*
     * メール
     */
    // from
    String MAIL_FROM = "tweetsearchinfo@gmail.com";
    // cc
    String MAIL_CC = "tweetsearchinfo@gmail.com";

    // アカウント新規作成
    String ACCOUNT_NEW_CREATE_MAIL_SUBJECT = "アカウント新規作成手続き"; // 件名
    String ACCOUNT_NEW_CREATE_MAIL_TEXT =
            "下記にて本登録のお手続きをお願い致します。\n\n"
            + "URL : ${urlPath}/accountNewCreate/create?token=${token}"; // 本文

    // パスワード新規作成
    String PASSWORD_NEW_CREATE_MAIL_SUBJECT = "パスワード再発行手続き"; // 件名
    String PASSWORD_NEW_CREATE_MAIL_TEXT =
            "下記にてパスワードの再発行をお願い致します。\n\n"
            + "URL : ${urlPath}/passwordNewCreate/create?token=${token}"; // 本文

    // メールアドレス新規作成
    String EMAIL_UPDATE_SUBJECT = "メールアドレス新規作成手続き"; // 件名
    String EMAIL_UPDATE_MAIL_TEXT = "下記URLにて更新を確定してください。\n\n"
            + "URL : ${urlPath}/account/emailUpdate?token=${token}"; // 本文


    /*
     * 登録、更新時のflushメッセージ
     */

    // パスワード更新
    String PASSWORD_UPDATE = "パスワードを更新しました";

}
