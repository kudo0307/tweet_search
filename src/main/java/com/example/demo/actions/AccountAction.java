package com.example.demo.actions;

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

import com.example.demo.actions.views.AccountConverter;
import com.example.demo.constants.ForwardConst;
import com.example.demo.constants.JpaConst;
import com.example.demo.models.Account;
import com.example.demo.models.form.FormAccount;
import com.example.demo.models.form.FormAccount.EditData;
import com.example.demo.models.form.FormAccount.UpdateData;
import com.example.demo.services.AccountService;

@Controller
public class AccountAction extends ActionBase {

    @Autowired
    private AccountService acService;
    @Autowired
    private HttpSession session;

    @RequestMapping("/account")
    public String index(@PageableDefault(page = 0, size = JpaConst.ROW_PER_PAGE)Pageable pageable,Model model) {
        // アカウントデータ取得
        Page<Account> acPage = acService.getAll(pageable);

        model.addAttribute("page", acPage);
        model.addAttribute("accounts",acPage.getContent());

        // アカウント一覧画面へ
        return ForwardConst.ACCOUNT_INDEX_PAGE;
    }

    @GetMapping("/account/edit")
    public String edit(@ModelAttribute @Validated(EditData.class) FormAccount fac,BindingResult result ,Model model,@AuthenticationPrincipal Account loginAc) {
        if(result.hasErrors()) {
            // エラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        Account ac = acService.getById(fac.getId());

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

    // アカウントデータ更新
    @PostMapping("/account/update")
    public String update(@ModelAttribute @Validated(UpdateData.class) FormAccount fac,BindingResult result,Account ac ,Model model) {


        if(result.hasErrors()) {
            // アカウント編集画面へ
            return ForwardConst.ACCOUNT_EDIT_PAGE;
        }

        // 更新
        Account saveAc = acService.update(ac,fac);

        model.addAttribute("formAccount",AccountConverter.toForm(saveAc));

        // メール送信画面
        return ForwardConst.ACCOUNT_EDIT_EMAIL_SEND_MAIL;
    }

}
