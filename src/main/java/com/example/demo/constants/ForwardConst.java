package com.example.demo.constants;

// 画面遷移に関する値を定義するファイル
public interface ForwardConst {

    // html
    // エラー画面
    String ERR_UNKNOWN_PAGE = "views/error/unknown";
    String ERR_TOKEN_PAGE = "views/error/token-error";

    // アカウント
    String ACCOUNT_INDEX_PAGE = "views/accounts/index";
    String ACCOUNT_EDIT_PAGE = "views/accounts/edit";

    // アカウント新規作成
    String ACCOUNT_NEW_INDEX_PAGE = "views/accounts/newcreate/index";
    String ACCOUNT_NEW_CREATE_PAGE = "views/accounts/newcreate/create";
    String ACCOUNT_NEW_CREATE_COMPLETE = "views/accounts/newcreate/complete";
    String ACCOUNT_NEW_CREATE_SEND_MAIL = "views/accounts/newcreate/send-mail";

    // パスワード新規作成
    String PASSWORD_NEW_INDEX_PAGE = "views/password/index";
    String PASSWORD_NEW_CREATE_PAGE = "views/password/create";
    String PASSWORD_NEW_CREATE_COMPLETE = "views/password/complete";
    String PASSWORD_NEW_CREATE_SEND_MAIL = "views/password/send-mail";





}
