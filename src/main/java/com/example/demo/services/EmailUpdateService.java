package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.models.EmailUpdate;
import com.example.demo.models.form.FormAccount;
import com.example.demo.repositorys.AccountRepository;
import com.example.demo.repositorys.EmailUpdateRepository;

@Service
public class EmailUpdateService {

    @Autowired
    private AccountRepository acRepository;

    @Autowired
    private EmailUpdateRepository emuRepository;

    @Autowired
    private OnetimePasswordService otpService;


    // アカウントIDを元にデータを取得
    // @param id アカウントid
    // @return EmailUpdateデータ
    public EmailUpdate getByAccountId(Integer id) {
        return emuRepository.findByDeletedAtIsNullANDAccIdIs(id);
    }

    // トークンを元にデータを取得
    // @param token トークン
    // @return EmailUpdateデータ
    public EmailUpdate getByToken(String token) {
        return emuRepository.findByDeletedAtIsNullANDTokenIs(token);
    }

    // メールアドレス更新テーブルへデータ登録、更新する
    // @param emu メールアドレス更新データ
    // @return 登録、更新したAccountNewEmailデータ
    public EmailUpdate ancSave(EmailUpdate emu) {
        return emuRepository.save(emu);
    }

    /* メールアドレス更新テーブル登録
     * @param ac Accountデータ
     * @param fac FormAccountデータ
     * @return 登録したメールアドレス新規作成データ
     */
    public EmailUpdate create(Account ac , FormAccount fac) {
        try {

            // 既に登録されているワンタイムパスワードデータを削除する
            otpService.deleteOnetimePassword(getByAccountId(ac.getId()).getOtp());
        }catch(Exception e) {

        }
        EmailUpdate emu = new EmailUpdate();
        emu.setAc(ac); // アカウントデータセット
        emu.setEmail(fac.getEmail()); // メールアドレスセット
        emu.setOtp(otpService.createOnetimePassword()); // ワンタイムパスワードデータセット

        return ancSave(emu); // 登録
    }
}
