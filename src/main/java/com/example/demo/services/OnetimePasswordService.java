package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return repository.findByTokenIs(token);
    }

    // ワンタイムパスワードを登録、更新する
    // @param ワンタイムパスワードデータ
    // @return 登録、更新したワンタイムパスワードデータ
    public OnetimePassword otpSave(OnetimePassword otp) {
        return repository.save(otp);
    }
}
