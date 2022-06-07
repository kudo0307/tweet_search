package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AccountNewCreate;
import com.example.demo.repositorys.AccountNewCreateRepository;
import com.example.demo.repositorys.AccountRepository;

@Service
public class AccountNewCreateService {

    @Autowired
    private AccountRepository acRepository;

    @Autowired
    private AccountNewCreateRepository ancRepository;


    // メールアドレスを元にデータを取得
    // @param email メールアドレス
    // @return AccountNewCreateデータ
    public AccountNewCreate getByEmail(String email) {
        return ancRepository.findByDeletedAtIsNullANDEmailIs(email);
    }

    // トークンを元にデータを取得
    // @param token トークン
    // @return AccountNewCreateデータ
    public AccountNewCreate getByToken(String token) {
        return ancRepository.findByDeletedAtIsNullANDTokenIs(token);
    }

    // アカウント新規作成テーブルへデータ登録、更新する
    // @param anc アカウント新規作成データ
    // @return 登録、更新したアカウント新規作成データ
    public AccountNewCreate ancSave(AccountNewCreate anc) {
        return ancRepository.save(anc);
    }
}
