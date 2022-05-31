package com.example.demo.actions;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;

@Controller
public abstract class ActionBase {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ServletContext context;

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
        return request.getSession().getId();
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

    // pepper文字列を取得する
    // @return pepper文字列
    @SuppressWarnings("unchecked")
    protected <R> R getPepper() {
        return (R) context.getAttribute("pepper");
    }
}
