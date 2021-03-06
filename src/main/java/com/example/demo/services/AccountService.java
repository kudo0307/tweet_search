package com.example.demo.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.constants.JpaConst;
import com.example.demo.models.Account;
import com.example.demo.models.EmailUpdate;
import com.example.demo.models.PasswordNewCreate;
import com.example.demo.models.form.FormAccount;
import com.example.demo.repositorys.AccountRepository;
import com.example.demo.utils.EncryptUtil;

// アカウントテーブルの操作にかかわる処理を行うクラス

@Service
public class AccountService implements UserDetailsService{

    @Autowired
    private AccountRepository acRepository;

    // 件数を絞り込んだアカウントデータ取得
    // @return 件数を絞り込んだアカウントデータ
    public Page<Account> getAll(Pageable pageable){
        return acRepository.findAccountData(pageable);
    }

    // IDを元にアカウントデータを一件取得
    // @param id アカウントid
    // @return Accountデータ
    public Account getById(Integer id){
        return acRepository.findByDeletedAtIsNullById(id);
    }

    /* ゲストアカウントを取得
     * @return ゲストアカウントデータ
     */
    public Account getGuest() {
        return acRepository.findByDeletedAtIsNullANDFlagIs(JpaConst.ROLE_GUEST);
    }

    // メールアドレスを元にデータを取得
    // @param email メールアドレス
    // @return Accountデータ
    public Account getByEmail(String email) {
        return acRepository.findByDeletedAtIsNullANDEmailIs(email);
    }

    // アカウントデータを登録、更新する
    // @param アカウントデータ
    // @return 登録、更新したアカウントデータ
    public Account acSave(Account ac) {
        return acRepository.save(ac);
    }

    // パスワード新規作成
    // @param fac フォーム用アカウントデータ
    // @param pnc パスワード新規作成データ
    public void passwordUpdate(FormAccount fac, PasswordNewCreate pnc) {

        // アカウントデータ取得
        Account ac = getById(pnc.getAc().getId());
        LocalDateTime now = LocalDateTime.now();
        ac.setPassword(EncryptUtil.passwordEncode(fac.getPassword())); // パスワード
        ac.setUpdatedAt(now); // 更新日
        acSave(ac); // 更新
    }

    /* メールアドレス更新
     * @param emu メールアドレス更新データ
     */
    public void emailUpdate(EmailUpdate emu) {
        Account saveAc = emu.getAc();
        saveAc.setEmail(emu.getEmail()); // メールアドレス
        saveAc.setUpdatedAt(LocalDateTime.now()); // 更新日
        acSave(saveAc); // 更新
    }

    /* パスワード更新
     * @param ac アカウントデータ
     * @param password パスワード
     */
    public Account passwordUpdate(Account ac,String password) {
        ac.setPassword(EncryptUtil.passwordEncode(password)); // パスワード
        ac.setUpdatedAt(LocalDateTime.now()); // 更新日
        return acSave(ac); // 更新
    }

    // アカウントデータ更新
    // @param ac アカウントデータ
    // @param fac 更新データ
    // @return 更新したアカウントデータ
    public Account update(Account ac , FormAccount fac) {
        Account saveAc = ac;

        saveAc.setEmail(fac.getEmail()); // メールアドレス

        if(fac.getPassword() != null) {
            saveAc.setPassword(EncryptUtil.passwordEncode(fac.getPassword())); // パスワード
        }

        ac.setUpdatedAt(LocalDateTime.now()); // 更新日
        return acSave(saveAc); // 更新

    }

    /* アカウントデータ削除
     * @param ac 削除対象アカウントデータ
     */
    public void delete(Account ac) {
        ac.setDeletedAt(LocalDateTime.now()); // 削除日をセット
        acSave(ac);
    }

    // ログインアカウント検索
    // @param email メールアドレス
    // @return spring securityのログインユーザーインスタンス
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account ac = new Account();
        try {
            ac = acRepository.findByDeletedAtIsNullANDEmailIs(email);
        }catch(Exception e) {
            // 取得時にExceptionが発生した場合
            throw new UsernameNotFoundException("It can not be acquired User");
        }

        // ユーザー情報を取得できなかった場合
        if(ac == null){
            throw new UsernameNotFoundException("メールアドレスまたはパスワードが間違っています");
        }

        return (UserDetails)ac;
    }

    /* ログイン情報を格納する
     * @param ac アカウント情報
     */
    public void addAuth(Account ac) {
        UserDetails userDetail = loadUserByUsername(ac.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), userDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
