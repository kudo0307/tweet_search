package com.example.demo.actions.views;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.models.Account;
import com.example.demo.models.Favorite;
import com.example.demo.models.Tweet;
import com.example.demo.models.form.TweetData;
import com.example.demo.services.FavoriteService;
import com.example.demo.services.TweetService;
import com.fasterxml.jackson.databind.JsonNode;

import twitter4j.MediaEntity;
import twitter4j.MediaEntity.Variant;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Component
public class TweetConverter {

    @Autowired
    private FavoriteService fvtService;
    @Autowired
    private TweetService tweService;

    /*ツイートデータのリスト生成
     * @param json ツイートデータのJsonNode
     * @return tweetDataList ツイートデータリスト
     * */
    public List<TweetData> jsonNodeToTweetList(JsonNode json) {
        List<TweetData> tweetDataList = new ArrayList<>(); // ツイートデータリスト

        // Twitterオブジェクト生成
        Twitter twitter = new TwitterFactory().getSingleton();
        for(int i=0;i<json.get("data").size(); i++) {

            // ツイートデータ取得
            TweetData tweetData = tweetIdToTweetData(json.get("data").get(i).get("id").asLong());
            if(tweetData != null) {
                tweetDataList.add(tweetData); // ツイートデータリストに追加
            }
        }
        return tweetDataList;
    }

    /* ツイートIDを元にツイートを取得して、TweetDataに変換して返す
     * @param id ツイートid
     * @return tweetData  ツイートデータ
     */
    public TweetData tweetIdToTweetData(long id){
        TweetData tweetData = new TweetData(); // ツイートデータ
        try {
            Twitter twitter = new TwitterFactory().getSingleton();
            Status twStatus = twitter.showStatus(id);
            tweetData.setText(twStatus.getText()); // ツイート本文をセット

            Tweet twe = new Tweet();
            for (MediaEntity mediaEntity : twStatus.getMediaEntities()) {
                twe.setUrl(mediaEntity.getExpandedURL()); // ツイートURLセット
                tweetData.setUrl(mediaEntity.getExpandedURL()); // ツイートURLセット
                // 動画URL取得
                for(Variant variant : mediaEntity.getVideoVariants()) {
                    // 動画URLか判定
                    if(variant.getUrl().indexOf(".mp4") != -1) {
                        tweetData.setVideoUrl(variant.getUrl()); // 動画URLセット
                    }
                }
            }
            twe.setTweetId(twStatus.getId()); // ツイートIDセット
            twe.setVideoUrl(tweetData.getVideoUrl()); // 動画URLセット

            // ツイートテーブルにデータを登録して、登録データをtweetDataにセット
            tweetData.setTwe(tweService.create(twe));

            // ログインアカウントを取得
            Account loginAccount = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // お気に入りデータ取得
            Favorite fvt = fvtService.search(id, loginAccount.getId());
            if(fvt!=null) {
                tweetData.setFavoriteFlag(fvt.isFlag()); // お気に入りフラグセット
            }else {
                // お気に入りデータが存在しない場合flagはfalseに
                tweetData.setFavoriteFlag(false);
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return tweetData;
    }
}
