package com.example.demo.actions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // サイトURL取得
    @Value("${tweet.search.url.path}")
    private String URL_PATH;

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

    @GetMapping("/account/delete")
    public String delete(@AuthenticationPrincipal Account loginAccount,@RequestParam(name = "id")Integer id,Model model,RedirectAttributes redirectAttributes) {
        // 管理者権限チェック
        if(!checkAdmin(loginAccount)) {
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        // 対象のidでデータを取得
        Account deleteAc = acService.getById(id);
        // 自分自身は削除できないようにする
        if(deleteAc != null &&deleteAc.getId() != loginAccount.getId()) {
            acService.delete(deleteAc); // データを削除
            // 削除完了メッセージ
            redirectAttributes.addFlashAttribute("flush",MessageConst.ACCOUNT_DELETE);
        }
        return "redirect:/account";
    }

    // アカウント編集画面
    @GetMapping("/account/edit")
    public String edit(@ModelAttribute @Validated(EditData.class) FormAccount fac,BindingResult result ,Model model,@AuthenticationPrincipal Account loginAccount) {

        if(result.hasErrors()|| loginAccount.getAdminFlag() == JpaConst.ROLE_GUEST) {
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

        // sessionへAccountデータを格納
        session.setAttribute("account", ac);

        // アカウント編集画面へ
        return ForwardConst.ACCOUNT_EDIT_PAGE;
    }

    // メールアドレスチェック
    @PostMapping("/account/emailCheck")
    public String emailCheck(@ModelAttribute @Validated(EmailUpdateData.class) FormAccount fac,BindingResult result,Model model,@AuthenticationPrincipal Account loginAccount,RedirectAttributes redirectAttributes) {

        if(loginAccount.getAdminFlag() == JpaConst.ROLE_GUEST) {
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
        textReplaceStrArr.put("urlPath",URL_PATH); // URLのPATHをセット
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

        // アカウントIDをセット
        redirectAttributes.addAttribute("id",saveEmu.getAc().getId());
        // メール送信画面
        return "redirect:/account/updateEmailSendMail";
    }
    @RequestMapping("/account/updateEmailSendMail")
    public String updateEmailSendMail(@RequestParam("id") Integer id ,Model model,@AuthenticationPrincipal Account loginAccount) {
        model.addAttribute("id",id);
        if(loginAccount.getAdminFlag() == JpaConst.ROLE_GUEST) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }
        return ForwardConst.ACCOUNT_EDIT_EMAIL_SEND_MAIL;
    }

    // メールアドレス更新
    @GetMapping("/account/emailUpdate")
    public String emailUpdate(@ModelAttribute @Validated(CreateData.class) OnetimePassword otp ,BindingResult result ,Model model,@AuthenticationPrincipal Account loginAccount) {
        if(result.hasErrors()||loginAccount.getAdminFlag() == JpaConst.ROLE_GUEST) {
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

                // リンク先切り替え用ステータスをセット
                model.addAttribute("pageStatus","accountEdit");
                // アカウントidをセット
                model.addAttribute("id",emu.getAc().getId());
                // 有効期限が過ぎていたら
                return ForwardConst.ERR_TOKEN_PAGE;
            }
        }

        // メールアドレスを更新する
        acService.emailUpdate(emu);

        // ワンタイムパスワードを削除する
        otpService.deleteOnetimePassword(emu.getOtp());

        // ログイン情報削除
        model.addAttribute("loginAccount",new Account());

        // sessionからログイン情報を削除
        SecurityContextHolder.clearContext();

        // メールアドレス更新完了画面
        return "redirect:/account/updateEmailComplete";
    }
    @RequestMapping("/account/updateEmailComplete")
    public String updateEmailComplete(@AuthenticationPrincipal Account loginAccount) {
        return ForwardConst.ACCOUNT_UPDATE_EMAIL;
    }

    // パスワード更新
    @PostMapping("/account/passwordUpdate")
    public String passwordCheck(@ModelAttribute @Validated(PasswordUpdateData.class) FormAccount fac,BindingResult result ,Model model,RedirectAttributes redirectAttributes,@AuthenticationPrincipal Account loginAccount) {
        if(loginAccount.getAdminFlag() == JpaConst.ROLE_GUEST) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

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

        // sessionのログイン情報を更新する
        acService.addAuth(saveAc);

        // 更新完了メッセージ
        redirectAttributes.addFlashAttribute("flush",MessageConst.PASSWORD_UPDATE);
        redirectAttributes.addFlashAttribute("formAccount",AccountConverter.toForm(saveAc));

        // セッションからアカウントデータを削除
        session.removeAttribute("account");
        // アカウント編集画面へ
        return "redirect:/account/edit?id="+saveAc.getId();
    }


}
