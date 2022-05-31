package com.example.demo.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ActionBase {

    protected HttpServletRequest request;
    protected HttpServletResponse response;

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

    /**
     * セッションIDを取得する
     * @return セッションID
     */
    protected String getTokenId() {
        return request.getSession().getId();
    }
}
