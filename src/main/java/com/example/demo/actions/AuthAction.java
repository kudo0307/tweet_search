package com.example.demo.actions;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constants.ForwardConst;
import com.example.demo.constants.MessageConst;

@Controller
public class AuthAction extends  ActionBase{

    @GetMapping("/login")
    public String login(Model model) {
        return ForwardConst.LOGIN_PAGE;
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        // セッションを破棄
        SecurityContextHolder.clearContext();
        redirectAttributes.addFlashAttribute("logout",MessageConst.LOGOUT_SUCCESS);
        return "redirect:/login";
    }

}
