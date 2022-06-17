package com.example.demo.actions.views;

import com.example.demo.models.Account;
import com.example.demo.models.form.FormAccount;

public class AccountConverter {
    /*
     * AccountからFormAccountへ変換
     * @param ac Accountデータ
     * @return FormAccountデータ
     */
    public static FormAccount toForm(Account ac) {
        FormAccount fac = new FormAccount();

        fac.setId(ac.getId()); // id
        fac.setEmail(ac.getEmail()); // メールアドレス
        fac.setPassword(ac.getPassword()); // パスワード

        return fac;
    }
}
