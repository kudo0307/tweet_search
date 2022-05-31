package com.example.demo.actions.views;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.Account;

// アカウントデータのDTOモデル⇔Viewモデルの変換を行うクラス
public class AccountConverter {

    // ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
    // @param acv AccountViewのインスタンス
    // @return Accountのインスタンス
    public static Account toModel(AccountView acv) {

        return new Account(
                acv.getId(),
                acv.getName(),
                acv.getEmail(),
                acv.getPassword(),
                acv.getAdminFlag(),
                LocalDateTime.parse(acv.getCreatedAt()),
                LocalDateTime.parse(acv.getUpdatedAt()),
                LocalDateTime.parse(acv.getDeletedAt())
                );
    }

    // DTOモデルのインスタンスからViewモデルのインスタンスを作成する
    // @param a Accountのインスタンス
    // @return AccountViewのインスタンス
    public static AccountView toView(Account a) {

        if(a == null) {
            return null;
        }

        return new AccountView(
                a.getId(),
                a.getName(),
                a.getEmail(),
                a.getPassword(),
                a.getAdminFlag(),
                a.getCreatedAt().toString(),
                a.getUpdatedAt().toString(),
                a.getDeletedAt().toString()
                );
    }

    // DTOモデルのリストからViewモデルのリストを作成する
    // @param list DTOモデルのリスト
    // @return Viewモデルのリスト
    public static List<AccountView> toViewList(List<Account> list){
        List<AccountView> acv = new ArrayList<>();

        for(Account a : list) {
            acv.add(toView(a));
        }

        return acv;
    }

    // Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
    // @param a DTOモデル(コピー先)
    // @param acv Viewモデル(コピー元)
    public static void copyViewToModel(Account a , AccountView acv) {
        a.setId(acv.getId());
        a.setName(acv.getName());
        a.setEmail(acv.getEmail());
        a.setPassword(acv.getPassword());
        a.setAdminFlag(acv.getAdminFlag());
        a.setCreatedAt(LocalDateTime.parse(acv.getCreatedAt()));
        a.setCreatedAt(LocalDateTime.parse(acv.getUpdatedAt()));
        a.setDeletedAt(LocalDateTime.parse(acv.getDeletedAt()));
    }
}
