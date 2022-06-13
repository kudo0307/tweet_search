package com.example.demo.actions;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.constants.ForwardConst;
import com.example.demo.constants.MessageConst;

@Controller
public class AuthAction extends  ActionBase{

    @GetMapping("/login")
    public String login() {
        return ForwardConst.LOGIN_PAGE;
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        // セッションを破棄
        SecurityContextHolder.clearContext();
        model.addAttribute("logout",MessageConst.LOGOUT_SUCCESS);
        return ForwardConst.LOGIN_PAGE;
    }

}
