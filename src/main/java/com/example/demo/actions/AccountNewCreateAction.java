package com.example.demo.actions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.constants.ForwardConst;
import com.example.demo.constants.JpaConst;
import com.example.demo.constants.MessageConst;
import com.example.demo.models.Account;
import com.example.demo.models.AccountNewCreate;
import com.example.demo.models.OnetimePassword;
import com.example.demo.models.form.FormAccount;
import com.example.demo.models.form.FormAccount.CreateData;
import com.example.demo.models.form.FormAccountNewCreate;
import com.example.demo.services.AccountNewCreateService;
import com.example.demo.services.AccountService;
import com.example.demo.services.OnetimePasswordService;

@Controller
public class AccountNewCreateAction extends ActionBase {


    @Autowired
    private AccountService acService;
    @Autowired
    private AccountNewCreateService ancService;
    @Autowired
    private OnetimePasswordService otpService;


    @RequestMapping("/accountNewCreate")
    public String newCreate(@ModelAttribute AccountNewCreate anc, Model model) {
        model.addAttribute("formAccountNewCreate",new FormAccountNewCreate());
        // アカウント新規作成画面へ遷移
        return ForwardConst.ACCOUNT_NEW_INDEX_PAGE;
    }

    @PostMapping("/accountNewCreate/checkEmail")
    public String checkEmail(@ModelAttribute @Validated FormAccountNewCreate fanc, BindingResult result, Model model) {

        if(result.hasErrors()) {
            // バリデーションエラーがあった場合

            // アカウント新規作成画面へ遷移
            return ForwardConst.ACCOUNT_NEW_INDEX_PAGE;
        }

        // メールアドレスを元にアカウントデータを取得
        Account ac = acService.getByEmail(fanc.getEmail());
        if(ac != null) {
            // 入力されたメールアドレスでアカウントが既に登録されていたら

            // エラーメッセージ
            model.addAttribute("error",MessageConst.ACCOUNT_ALREADY_EXISTS);
            // アカウント新規作成画面へ遷移
            return ForwardConst.ACCOUNT_NEW_INDEX_PAGE;
        }

        // メールアドレスを元にアカウント新規作成データを取得
        AccountNewCreate anc = ancService.getByEmail(fanc.getEmail());

        if(anc != null) {
            // 既に登録されているワンタイムパスワードデータを削除する
            OnetimePassword deleteOtp =  anc.getOtp();

            deleteOtp.setDeletedAt(LocalDateTime.now()); // 削除日
            otpService.otpSave(deleteOtp); // 更新
        }

        // ワンタイムパスワード作成
        OnetimePassword otp = createOnetimePassword();

        // アカウント新規作成テーブルへデータ登録
        AccountNewCreate saveAnc = new AccountNewCreate();

        saveAnc.setEmail(fanc.getEmail()); // メールアドレス
        saveAnc.setOtp(otp); // ワンタイムパスワードテーブルid
        AccountNewCreate createAnc = ancService.ancSave(saveAnc); // 登録

        // メール送信
        // 置換する文字の配列作成
        Map<String,String> textReplaceStrArr = new HashMap<>();
        textReplaceStrArr.put("token",createAnc.getOtp().getToken() ); // トークンをセット

        // メール本文取得
        String text = MessageConst.ACCOUNT_NEW_CREATE_MAIL_TEXT;

        sendMail(MessageConst.MAIL_FROM, // from
                createAnc.getEmail(), // to
                MessageConst.MAIL_CC, // cc
                MessageConst.ACCOUNT_NEW_CREATE_MAIL_SUBJECT, // 件名
                getMailBodyText(text,textReplaceStrArr)); // 本文




        // メール送信完了画面遷移
        return ForwardConst.ACCOUNT_NEW_CREATE_SEND_MAIL;
    }

    @GetMapping("/accountNewCreate/create")
    public String create(@ModelAttribute @Validated(CreateData.class) OnetimePassword otp ,BindingResult result ,Model model) {
        if(result.hasErrors()) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        // アカウント新規作成データ取得
        AccountNewCreate anc = ancService.getByToken(otp.getToken());

        if(anc == null) {
            // データが取得できなければ
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }else {
            // データ取得
            // 有効期限のチェック
            if(anc.getOtp().getTokenAt().isBefore(LocalDateTime.now())) {
                // 有効期限が過ぎていたら
                return ForwardConst.ERR_TOKEN_PAGE;
            }
        }

        model.addAttribute("accounNewCreate",anc);
        model.addAttribute("formAccount",new FormAccount());

        // sessionに値をセット
        session.setAttribute("accountNewCreate",anc);

        return ForwardConst.ACCOUNT_NEW_CREATE_PAGE;
    }

    @PostMapping("/accountNewCreate/create")
    public String create(@ModelAttribute @Validated(CreateData.class) FormAccount fac ,BindingResult result ,Model model) {
        if(result.hasErrors()) {

            return ForwardConst.ACCOUNT_NEW_CREATE_PAGE;
        }

        // sessionからAccountNewCreateのデータ取得
        AccountNewCreate anc = (AccountNewCreate) session.getAttribute("accountNewCreate");

        // 有効期限のチェック
        if(anc.getOtp().getTokenAt().isBefore(LocalDateTime.now())) {
            // 有効期限が過ぎていたら
            return ForwardConst.ERR_TOKEN_PAGE;
        }
        // アカウント新規作成テーブルへデータ登録
        Account saveAc = new Account();

        LocalDateTime now = LocalDateTime.now();
        saveAc.setName(fac.getName()); // アカウント名
        saveAc.setEmail(anc.getEmail()); // メールアドレス
        saveAc.setPassword(passwordEncode(fac.getPassword())); // パスワード
        saveAc.setAdminFlag(JpaConst.ROLE_GENERAL); // 一般権限
        saveAc.setCreatedAt(now); // 作成日
        saveAc.setUpdatedAt(now); // 更新日
        acService.acSave(saveAc); // 登録

        // ワンタイムパスワードを削除する
        OnetimePassword saveOtp = new OnetimePassword();
        saveOtp.setId(anc.getOtp().getId()); // id
        saveOtp.setToken(anc.getOtp().getToken()); // トークン
        saveOtp.setTokenAt(anc.getOtp().getTokenAt()); // トークン期限
        saveOtp.setDeletedAt(LocalDateTime.now()); // 削除日
        otpService.otpSave(saveOtp); // 更新




        // アカウント登録完了画面
        return ForwardConst.ACCOUNT_NEW_CREATE_COMPLETE;
    }


}
