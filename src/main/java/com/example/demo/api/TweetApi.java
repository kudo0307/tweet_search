package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.ApiConst;
import com.example.demo.constants.JpaConst;
import com.example.demo.models.Account;
import com.example.demo.models.Favorite;
import com.example.demo.models.Tweet;
import com.example.demo.models.api.TweetApiData;
import com.example.demo.services.FavoriteService;
import com.example.demo.services.TweetService;

@RestController
@RequestMapping("favorite")
public class TweetApi {

    @Autowired
    private FavoriteService fvtService;
    @Autowired
    private TweetService tweService;

    @RequestMapping("/save")
    public TweetApiData save(@RequestParam long tweetId,@AuthenticationPrincipal Account loginAccount) {

        TweetApiData tad = new TweetApiData();


        if(loginAccount.getAdminFlag() == JpaConst.ROLE_GUEST) {
            // ゲストアカウントなら処理しない
            return tad;
        }

        Tweet twe = tweService.search(tweetId);
        if(twe == null) {
            // エラー
            tad.setId(null);
            tad.setStatus(ApiConst.FAVORITE_ERROR); // ステータスセット
            return tad;
        }

        // ツイートIDとログインIDを元にお気に入りデータを取得
        Favorite fvt = fvtService.search(tweetId, loginAccount.getId());

        if(fvt ==null) {
            tad.setId(String.valueOf(twe.getTweetId())); // ツイートIDセット
            // データが取得できなければ登録する
            fvtService.create(twe, loginAccount);
            tad.setStatus(ApiConst.FAVORITE_REGISTER); // ステータスセット

        }else {
            // データが取得できた場合
            Favorite saveFvt = fvtService.update(fvt);
            tad.setId(String.valueOf(fvt.getTwe().getTweetId())); // ツイートIDセット
            if(saveFvt.isFlag()) {
                // お気に入り登録
                tad.setStatus(ApiConst.FAVORITE_REGISTER);
            }else {
                // お気に入り解除
                tad.setStatus(ApiConst.FAVORITE_RELEASE);
            }
        }

        return tad;
    }
}
