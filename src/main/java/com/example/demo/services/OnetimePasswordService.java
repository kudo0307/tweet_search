package com.example.demo.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.actions.ActionBase;
import com.example.demo.constants.JpaConst;
import com.example.demo.models.OnetimePassword;
import com.example.demo.repositorys.OnetimePasswordRepository;

@Service
public class OnetimePasswordService {
    @Autowired
    private OnetimePasswordRepository repository;

    // トークンを元にワンタイムパスワードデータを取得
    // @param token トークン
    // @return ワンタイムパスワードデータ
    public OnetimePassword getByToken(String token) {
        LocalDateTime now = LocalDateTime.now();
        return repository.findByTokenIsAndTokenAtBefore(token,now);
    }

    // 既に登録されているワンタイムパスワードを削除する
    public void deleteOnetimePassword(OnetimePassword deleteOtp) {
        if(deleteOtp==null) {
            return;
        }
        deleteOtp.setDeletedAt(LocalDateTime.now()); // 削除日
        otpSave(deleteOtp); // 更新
    }

    // ワンタイムパスワードを登録する
    // @return createOtp 登録したワンタイムパスワードデータ
    public OnetimePassword createOnetimePassword() {
        OnetimePassword createOtp = new OnetimePassword();
        int count = 0;
        while(true) {
            if(count>100) {
                break;
            }
            String token = ActionBase.randomString(JpaConst.ONETIME_PASS_INT,JpaConst.ONETIME_PASS_STR);

            if(getByToken(token) == null) {

                // ワンタイムパスワードテーブルへデータ登録

                OnetimePassword saveOtp = new OnetimePassword();

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime tokenAt = now.plusMinutes(JpaConst.OTP_TOKENAT_MINUTE); // トークン期限作成

                saveOtp.setToken(token); // トークン
                saveOtp.setTokenAt(tokenAt); // トークン期限

                createOtp = otpSave(saveOtp); // 登録
                break;
            }
            count++;
        }

        return createOtp;

    }

    // ワンタイムパスワードを登録、更新する
    // @param ワンタイムパスワードデータ
    // @return 登録、更新したワンタイムパスワードデータ
    public OnetimePassword otpSave(OnetimePassword otp) {
        return repository.save(otp);
    }
}
