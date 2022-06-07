package com.example.demo.actions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.example.demo.actions.views.AccountView;
import com.example.demo.constants.JpaConst;
import com.example.demo.models.OnetimePassword;
import com.example.demo.services.OnetimePasswordService;

@Controller
public abstract class ActionBase {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;

    @Autowired
    private OnetimePasswordService otpservice;

    // CSRF対策 token不正の場合はエラー画面を表示
     // @return true: token有効 false: token不正
     // @throws ServletException
     // @throws IOException

    protected boolean checkToken() throws ServletException, IOException {

        //パラメータからtokenの値を取得
        String _token = request.getParameter("_token");

        if (_token == null || !(_token.equals(getTokenId()))) {
            return false;
        } else {
            return true;
        }

    }


     // セッションIDを取得する
     // @return セッションID

    protected String getTokenId() {
        return session.getId();
    }
    @Autowired
    private MailSender sender;

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
    protected String randomString(int count,String str) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(str.length());
            sb.append(str.charAt(num));
        }
        return sb.toString();
    }

    // ワンタイムパスワードを登録する
    // @return createOtp 登録したワンタイムパスワードデータ
    protected OnetimePassword createOnetimePassword() {



        OnetimePassword createOtp = new OnetimePassword();
        while(true) {
            String token = randomString(JpaConst.ONETIME_PASS_INT,JpaConst.ONETIME_PASS_STR);

            if(otpservice.getByToken(token) == null) {

                // ワンタイムパスワードテーブルへデータ登録

                OnetimePassword saveOtp = new OnetimePassword();

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime tokenAt = now.plusMinutes(JpaConst.OTP_TOKENAT_MINUTE); // トークン期限作成

                saveOtp.setToken(token); // トークン
                saveOtp.setTokenAt(tokenAt); // トークン期限

                createOtp = otpservice.otpSave(saveOtp); // 登録
                break;
            }
        }

        return createOtp;

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

    // ログイン中のアカウントが管理者かどうかチェック
    // true: 管理者 , false: 管理者ではない
    // @throws ServletException
    // @throws IOException
    protected boolean checkAdmin() throws ServletException,IOException{

        // セッションからログイン中のアカウント情報を取得
        AccountView acv = (AccountView)session.getAttribute("accounts");

        if(acv.getAdminFlag() != JpaConst.ROLE_ADMIN) {
            return false;
        }else {
            return true;
        }
    }

    // パスワードをハッシュ化する
    // @param password ハッシュ化するパスワード
    // @return ハッシュ化したパスワード
    protected String passwordEncode(String password) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;
        return passwordEncoder.encode(password);
    }
}
