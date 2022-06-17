package com.example.demo.constants;

// 画面遷移に関する値を定義するファイル
public interface ForwardConst {

    // html
    // エラー画面
    String ERR_UNKNOWN_PAGE = "views/error/unknown";
    String ERR_TOKEN_PAGE = "views/error/token-error";

    // ログイン画面
    String LOGIN_PAGE = "views/login/login.html";

    // アカウント
    // 一覧
    String ACCOUNT_INDEX_PAGE = "views/account/index";
    // 編集
    String ACCOUNT_EDIT_PAGE = "views/account/edit";
    // メール送信
    String ACCOUNT_EDIT_EMAIL_SEND_MAIL = "views/account/send-mail";
    // メールアドレス更新
    String ACCOUNT_UPDATE_EMAIL = "views/account/email-update";

    // アカウント新規作成
    String ACCOUNT_NEW_INDEX_PAGE = "views/account/newcreate/index";
    String ACCOUNT_NEW_CREATE_PAGE = "views/account/newcreate/create";
    String ACCOUNT_NEW_CREATE_COMPLETE = "views/account/newcreate/complete";
    String ACCOUNT_NEW_CREATE_SEND_MAIL = "views/account/newcreate/send-mail";

    // パスワード新規作成
    String PASSWORD_NEW_INDEX_PAGE = "views/password/index";
    String PASSWORD_NEW_CREATE_PAGE = "views/password/create";
    String PASSWORD_NEW_CREATE_COMPLETE = "views/password/complete";
    String PASSWORD_NEW_CREATE_SEND_MAIL = "views/password/send-mail";






}
