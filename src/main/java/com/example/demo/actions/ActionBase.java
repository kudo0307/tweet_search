package com.example.demo.actions;

import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.constants.JpaConst;
import com.example.demo.models.Account;
import com.example.demo.services.AccountService;

@ControllerAdvice
public abstract class ActionBase {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;
    @Autowired
    private MailSender sender;
    @Autowired
    private AccountService acService;
    // デフォルトデータセット
    @ModelAttribute
    public void addDefaults(@AuthenticationPrincipal Account loginAccount,Model model) {

        if(loginAccount == null) {
            // sessionにログイン情報がない場合ゲストアカウントをログイン情報に格納
            loginAccount = acService.getGuest();
            acService.addAuth(loginAccount);
        }
        // ログインデータセット
        model.addAttribute("loginAccount",loginAccount);
        // 管理者権限
        model.addAttribute("roleAdmin",JpaConst.ROLE_ADMIN);
    }

    // 管理者権限チェック
    protected static boolean checkAdmin(@AuthenticationPrincipal Account loginAccount) {
        if(loginAccount.getAdminFlag() != JpaConst.ROLE_ADMIN) {
            return false;
        }else {
            return true;
        }
    }

    // メールを送信する
    // @param from 送信元メールアドレス
    // @param to 送信先メールアドレス
    // @param cc cc ない場合はNULL
    // @param subject 件名
    // @param text 本文
    protected void sendMail(String from, String to, String cc, String subject, String text) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from); // 送信元メールアドレス
        msg.setTo(to); // 送信先メールアドレス
        if(cc != null){
            msg.setCc(cc); // cc
        }

        msg.setSubject(subject); // 件名
        msg.setText(text); //本文

        try {
            this.sender.send(msg);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    // ランダムな文字列を生成
    // @param count 文字数
    // @param str 使用する文字列
    // @return ランダム文字列
    public static String randomString(int count,String str) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(str.length());
            sb.append(str.charAt(num));
        }
        return sb.toString();
    }

    // メールの文章を生成する
    // @param text メール本文
    // @param
    protected String getMailBodyText(String text , Map<String, String> textReplaceStrArr) {

        if(text.isBlank() || textReplaceStrArr == null) {
            return "";
        }

        for(Map.Entry<String, String> entry : textReplaceStrArr.entrySet()){
            text = text.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return text;
    }


     // 文字列を数値に変換する
     // @param strNumber 変換前文字列
     // @return 変換後数値

    protected int toNumber(String strNumber) {
        int number = 0;
        try {
            number = Integer.parseInt(strNumber);
        } catch (Exception e) {
            number = Integer.MIN_VALUE;
        }
        return number;
    }
}
