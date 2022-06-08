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
import com.example.demo.constants.MessageConst;
import com.example.demo.models.Account;
import com.example.demo.models.OnetimePassword;
import com.example.demo.models.PasswordNewCreate;
import com.example.demo.models.form.FormAccount;
import com.example.demo.models.form.FormAccount.CreateData;
import com.example.demo.models.form.FormAccount.NewPasswordData;
import com.example.demo.models.form.FormPasswordNewCreate;
import com.example.demo.services.AccountService;
import com.example.demo.services.OnetimePasswordService;
import com.example.demo.services.PasswordNewCreateService;

@Controller
public class PasswordNewCreateAction extends ActionBase {


    @Autowired
    private AccountService acService;
    @Autowired
    private PasswordNewCreateService pncService;
    @Autowired
    private OnetimePasswordService otpService;


    @RequestMapping("/passwordNewCreate")
    public String newCreate(@ModelAttribute PasswordNewCreate pnc, Model model) {
        model.addAttribute("formPasswordNewCreate",new FormPasswordNewCreate());
        // パスワード新規作成画面へ遷移
        return ForwardConst.PASSWORD_NEW_INDEX_PAGE;
    }

    @PostMapping("/passwordNewCreate/checkEmail")
    public String checkEmail(@ModelAttribute @Validated FormPasswordNewCreate fpnc, BindingResult result, Model model) {

        if(result.hasErrors()) {
            // バリデーションエラーがあった場合

            // パスワード新規作成画面へ遷移
            return ForwardConst.PASSWORD_NEW_INDEX_PAGE;
        }

        // メールアドレスを元にアカウントデータを取得
        Account ac = acService.getByEmail(fpnc.getEmail());
        if(ac == null) {
            // 入力されたメールアドレスが登録されていなければ

            // エラーメッセージ
            model.addAttribute("error",MessageConst.ACCOUNT_NOT_EXISTS);
            // パスワード新規作成画面へ遷移
            return ForwardConst.PASSWORD_NEW_INDEX_PAGE;
        }

        // アカウントidを元にパスワード新規作成データを取得
        PasswordNewCreate pnc = pncService.getByAccountId(ac.getId());

        // 既に登録されているワンタイムパスワードデータを削除する
        otpService.deleteOnetimePassword(pnc);

        // ワンタイムパスワード作成
        OnetimePassword otp = otpService.createOnetimePassword();

        // パスワード新規作成テーブルへデータ登録
        PasswordNewCreate createPnc = pncService.create(ac, otp); // 登録

        // メール送信
        // 置換する文字の配列作成
        Map<String,String> textReplaceStrArr = new HashMap<>();
        textReplaceStrArr.put("token",createPnc.getOtp().getToken() ); // トークンをセット

        // メール本文取得
        String text = MessageConst.PASSWORD_NEW_CREATE_MAIL_TEXT;

        sendMail(MessageConst.MAIL_FROM, // from
                createPnc.getAc().getEmail(), // to
                MessageConst.MAIL_CC, // cc
                MessageConst.PASSWORD_NEW_CREATE_MAIL_SUBJECT, // 件名
                getMailBodyText(text,textReplaceStrArr)); // 本文




        // メール送信完了画面遷移
        return ForwardConst.PASSWORD_NEW_CREATE_SEND_MAIL;
    }

    @GetMapping("/passwordNewCreate/create")
    public String create(@ModelAttribute @Validated(CreateData.class) OnetimePassword otp ,BindingResult result ,Model model) {
        if(result.hasErrors()) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        // パスワード新規作成データ取得
        PasswordNewCreate pnc = pncService.getByToken(otp.getToken());

        if(pnc == null) {
            // データが取得できなければ
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }else {
            // データ取得
            // 有効期限のチェック
            if(pnc.getOtp().getTokenAt().isBefore(LocalDateTime.now())) {
                // 有効期限が過ぎていたら
                return ForwardConst.ERR_TOKEN_PAGE;
            }
        }

        model.addAttribute("passwordNewCreate",pnc);
        model.addAttribute("formAccount",new FormAccount());

        // sessionに値をセット
        session.setAttribute("passwordNewCreate",pnc);
        return ForwardConst.PASSWORD_NEW_CREATE_PAGE;
    }

    @PostMapping("/passwordNewCreate/create")
    public String create(@ModelAttribute @Validated(NewPasswordData.class) FormAccount fac ,BindingResult result ,Model model) {
        if(result.hasErrors()) {

            return ForwardConst.PASSWORD_NEW_CREATE_PAGE;
        }

        // sessionからPasswordNewCreateのデータ取得
        PasswordNewCreate pnc = (PasswordNewCreate) session.getAttribute("passwordNewCreate");


        // 有効期限のチェック
        if(pnc.getOtp().getTokenAt().isBefore(LocalDateTime.now())) {
            // 有効期限が過ぎていたら
            return ForwardConst.ERR_TOKEN_PAGE;
        }
        // パスワードを更新する
        acService.passwordUpdate(fac, pnc);

        // ワンタイムパスワードを削除する
        otpService.deleteOnetimePassword(pnc);

        // パスワード登録完了画面
        return ForwardConst.ACCOUNT_NEW_CREATE_COMPLETE;
    }


}
