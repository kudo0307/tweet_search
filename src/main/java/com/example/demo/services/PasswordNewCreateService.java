package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.models.OnetimePassword;
import com.example.demo.models.PasswordNewCreate;
import com.example.demo.repositorys.AccountRepository;
import com.example.demo.repositorys.PasswordNewCreateRepository;

@Service
public class PasswordNewCreateService {

    @Autowired
    private AccountRepository acRepository;

    @Autowired
    private PasswordNewCreateRepository pncRepository;


    // アカウントidを元にデータを取得
    // @param id アカウントid
    // @return PasswordNewCreateデータ
    public PasswordNewCreate getByAccountId(Integer id) {
        return pncRepository.findByDeletedAtIsNullANDIdIs(id);
    }

    // トークンを元にデータを取得
    // @param token トークン
    // @return PasswordNewCreateデータ
    public PasswordNewCreate getByToken(String token) {
        return pncRepository.findByDeletedAtIsNullANDTokenIs(token);
    }

    // パスワード新規作成テーブルへデータ登録
    // @param ac アカウントデータ
    // @param otp ワンタイムパスワードデータ
    // @return 登録したパスワード新規作成データ
    public PasswordNewCreate create(Account ac, OnetimePassword otp) {
        PasswordNewCreate savePnc = new PasswordNewCreate();

        savePnc.setAc(ac); // アカウントテーブルid
        savePnc.setOtp(otp); // ワンタイムパスワードテーブルid

        return pncSave(savePnc);// 登録
    }

    // パスワード新規作成テーブルへデータ登録、更新する
    // @param pnc パスワード新規作成データ
    // @return 登録、更新したパスワード新規作成データ
    public PasswordNewCreate pncSave(PasswordNewCreate pnc) {
        return pncRepository.save(pnc);
    }
}
