package com.example.demo.actions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.actions.views.AccountConverter;
import com.example.demo.constants.CommonConst;
import com.example.demo.constants.ForwardConst;
import com.example.demo.constants.JpaConst;
import com.example.demo.constants.MessageConst;
import com.example.demo.models.Account;
import com.example.demo.models.EmailUpdate;
import com.example.demo.models.OnetimePassword;
import com.example.demo.models.form.FormAccount;
import com.example.demo.models.form.FormAccount.CreateData;
import com.example.demo.models.form.FormAccount.EditData;
import com.example.demo.models.form.FormAccount.EmailUpdateData;
import com.example.demo.models.form.FormAccount.PasswordUpdateData;
import com.example.demo.services.AccountService;
import com.example.demo.services.EmailUpdateService;
import com.example.demo.services.OnetimePasswordService;

@Controller
public class AccountAction extends ActionBase {

    @Autowired
    private AccountService acService;
    @Autowired
    private EmailUpdateService emuService;
    @Autowired
    private OnetimePasswordService otpService;
    @Autowired
    private HttpSession session;

    // アカウント一覧
    @RequestMapping("/account")
    public String index(@PageableDefault(page = 0, size = JpaConst.ROW_PER_PAGE)Pageable pageable,@AuthenticationPrincipal Account loginAccount,Model model) {
        // 管理者権限チェック
        if(!checkAdmin(loginAccount)) {
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        // アカウントデータ取得
        Page<Account> acPage = acService.getAll(pageable);

        model.addAttribute("page", acPage);
        model.addAttribute("accounts",acPage.getContent());

        // アカウント一覧画面へ
        return ForwardConst.ACCOUNT_INDEX_PAGE;
    }

    // アカウント編集画面
    @GetMapping("/account/edit")
    public String edit(@ModelAttribute @Validated(EditData.class) FormAccount fac,BindingResult result ,Model model,@AuthenticationPrincipal Account loginAccount) {

        if(result.hasErrors()) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }
        Account ac = new Account();
        // 管理者権限チェック
        if(checkAdmin(loginAccount)) {
            // 管理者権限の場合
            // 指定されたIDのアカウントデータ取得
            ac = acService.getById(fac.getId());
        }else {
            // 一般権限の場合
            // 自分のアカウントデータ取得
            ac = acService.getById(loginAccount.getId());
        }

        if(ac == null) {
            // データが取得できなければ
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        model.addAttribute("formAccount", AccountConverter.toForm(ac));

        // 二重送信防止用トークン
        String protectionToken = randomString(CommonConst.PROTECTION_TOKEN_INT,CommonConst.PROTECTION_TOKEN_STR);
        model.addAttribute("protectionToken",protectionToken);

        // sessionへAccountデータを格納
        session.setAttribute("account", ac);
        // sessionへ二重送信防止用トークンを格納
        session.setAttribute("protectionToken", protectionToken);

        // アカウント編集画面へ
        return ForwardConst.ACCOUNT_EDIT_PAGE;
    }

    // メールアドレスチェック
    @PostMapping("/account/emailCheck")
    public String emailCheck(@ModelAttribute @Validated(EmailUpdateData.class) FormAccount fac,BindingResult result ,@RequestParam(name = "protectionToken")String protectionToken,Model model,@AuthenticationPrincipal Account loginAccount) {

        // 二重送信チェック
        if(session.getAttribute("protectionToken") == null ||session.getAttribute("protectionToken").toString().equals(protectionToken)) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        if(result.hasErrors()) {
            // アカウント編集画面へ
            return ForwardConst.ACCOUNT_EDIT_PAGE;
        }

        // sessionからアカウントデータを取得
        Account ac = (Account) session.getAttribute("account");

        // 既に登録されているメールアドレスか判定
        if(acService.getByEmail(fac.getEmail()) != null) {
            model.addAttribute("emailError",MessageConst.VALID_EMAIL_EXISTS);
            return ForwardConst.ACCOUNT_EDIT_PAGE;
        }

        EmailUpdate saveEmu = emuService.create(ac, fac); // メールアドレス更新データ作成

        // メール送信
        // 置換する文字の配列作成
        Map<String,String> textReplaceStrArr = new HashMap<>();
        textReplaceStrArr.put("token",saveEmu.getOtp().getToken() ); // トークンをセット

        // メール本文取得
        String text = MessageConst.EMAIL_UPDATE_MAIL_TEXT;

        sendMail(MessageConst.MAIL_FROM, // from
                saveEmu.getAc().getEmail(), // to
                MessageConst.MAIL_CC, // cc
                MessageConst.EMAIL_UPDATE_SUBJECT, // 件名
                getMailBodyText(text,textReplaceStrArr)); // 本文

        // セッションからアカウントデータを削除
        session.removeAttribute("account");
        // sessionからトークン削除
        session.removeAttribute("protectionToken");
        // メール送信画面
        return ForwardConst.ACCOUNT_EDIT_EMAIL_SEND_MAIL;
    }

    // メールアドレス更新
    @GetMapping("/account/emailUpdate")
    public String emailUpdate(@ModelAttribute @Validated(CreateData.class) OnetimePassword otp ,BindingResult result ,Model model) {
        if(result.hasErrors()) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        // メールアドレス更新データ取得
        EmailUpdate emu = emuService.getByToken(otp.getToken());

        if(emu == null) {
            // データが取得できなければ
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }else {
            // データ取得
            // 有効期限のチェック
            if(emu.getOtp().getTokenAt().isBefore(LocalDateTime.now())) {
                // 有効期限が過ぎていたら
                return ForwardConst.ERR_TOKEN_PAGE;
            }
        }

        // メールアドレスを更新する
        acService.emailUpdate(emu);

        // ワンタイムパスワードを削除する
        otpService.deleteOnetimePassword(emu.getOtp());

        // メールアドレス更新完了画面
        return ForwardConst.ACCOUNT_UPDATE_EMAIL;
    }

    // パスワード更新
    @PostMapping("/account/passwordUpdate")
    public String passwordCheck(@ModelAttribute @Validated(PasswordUpdateData.class) FormAccount fac,BindingResult result ,@RequestParam(name = "protectionToken")String protectionToken,Model model,RedirectAttributes redirectAttributes) {
        // sessionからアカウントデータを取得
        Account ac = (Account) session.getAttribute("account");
        if(ac == null) {
            model.addAttribute("flush","");
            model.addAttribute("formAccount",fac);
            return ForwardConst.ACCOUNT_EDIT_PAGE;
        }

        if(result.hasErrors()) {
            fac.setEmail(ac.getEmail());// メールアドレスをセット
            model.addAttribute("formAccount",fac);
            // アカウント編集画面へ
            return ForwardConst.ACCOUNT_EDIT_PAGE;
        }

        // パスワード更新
        Account saveAc = acService.passwordUpdate(ac, fac.getPassword());

        // 更新完了メッセージ
        redirectAttributes.addFlashAttribute("flush",MessageConst.PASSWORD_UPDATE);
        redirectAttributes.addFlashAttribute("formAccount",AccountConverter.toForm(saveAc));

        // セッションからアカウントデータを削除
        session.removeAttribute("account");
        // sessionからトークン削除
        session.removeAttribute("protectionToken");
        // アカウント編集画面へ
        return "redirect:/account/edit?id="+saveAc.getId();
    }


}
