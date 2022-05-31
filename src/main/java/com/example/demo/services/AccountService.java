package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.demo.actions.views.AccountConverter;
import com.example.demo.actions.views.AccountView;
import com.example.demo.repositorys.AccountRepository;

// アカウントテーブルの操作にかかわる処理を行うクラス

@Controller
public class AccountService{

    @Autowired
    private AccountRepository repository;

    public List<AccountView> getAll(){

        // 削除されていないデータを全件取得
        return AccountConverter.toViewList(repository.findByDeletedAtIsNull());
    }
}
