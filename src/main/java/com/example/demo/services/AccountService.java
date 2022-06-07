package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.actions.views.AccountConverter;
import com.example.demo.actions.views.AccountView;
import com.example.demo.models.Account;
import com.example.demo.repositorys.AccountRepository;

// アカウントテーブルの操作にかかわる処理を行うクラス

@Service
public class AccountService implements UserDetailsService{

    @Autowired
    private AccountRepository repository;

    // 削除されていないデータを全件取得
    // @return AccountViewのリスト
    public List<AccountView> getAll(){

        return AccountConverter.toViewList(repository.findAllByDeletedAtIsNullOrderDescById());
    }

    // IDを元にアカウントデータを一件取得
    // @param id アカウントid
    // @return AccountViewに変換したデータ
    public AccountView getById(Integer id){
        return AccountConverter.toView(repository.findByDeletedAtIsNullById(id));
    }

    // メールアドレスを元にデータを取得
    // @param email メールアドレス
    // @return Accountデータ
    public Account getByEmail(String email) {
        return repository.findByDeletedAtIsNullANDEmailIs(email);
    }

    // アカウントデータを登録、更新する
    // @param アカウントデータ
    // @return 登録、更新したアカウントデータ
    public Account acSave(Account ac) {
        return repository.save(ac);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account ac = repository.findByDeletedAtIsNullANDEmailIs(username);

        if(ac == null) {
            throw new UsernameNotFoundException("Email" + username + "was not found in the database");
        }

        // パスワードは渡すことができないので、暗号化
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return (UserDetails)new Account(
                ac.getId(),
                ac.getName(),
                ac.getEmail(),
                encoder.encode(ac.getPassword()),
                ac.getAdminFlag(),
                ac.getCreatedAt(),
                ac.getUpdatedAt(),
                ac.getDeletedAt()

                );
    }
}
