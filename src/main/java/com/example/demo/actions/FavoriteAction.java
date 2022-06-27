package com.example.demo.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.constants.ForwardConst;
import com.example.demo.constants.JpaConst;
import com.example.demo.models.Account;
import com.example.demo.models.Favorite;
import com.example.demo.services.FavoriteService;

@Controller
public class FavoriteAction extends  ActionBase{

    @Autowired
    private FavoriteService fvService;

    @RequestMapping("/favorite")
    public String list(@PageableDefault(page = 0, size = JpaConst.ROW_PER_PAGE)Pageable pageable,@AuthenticationPrincipal Account loginAccount,Model model) {

        if(loginAccount.getAdminFlag() == JpaConst.ROLE_GUEST) {
            // ゲストアカウントならエラー画面へ
            return ForwardConst.ERR_UNKNOWN_PAGE;
        }

        Page<Favorite> favorites = fvService.getList(pageable,loginAccount.getId());
        model.addAttribute("page", favorites);
        model.addAttribute("favorites",favorites.getContent());
        return ForwardConst.FAVORITE_LIST;
    }
}