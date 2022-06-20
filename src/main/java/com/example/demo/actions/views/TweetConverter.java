package com.example.demo.actions.views;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.models.TweetData;
import com.fasterxml.jackson.databind.JsonNode;

import twitter4j.MediaEntity;
import twitter4j.MediaEntity.Variant;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TweetConverter {
    /*ツイートデータのリスト生成
     * @param json ツイートデータのJsonNode
     * @return tweetDataList ツイートデータリスト
     * */
    public static List<TweetData> jsonNodeToTweetList(JsonNode json) {
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
    public static TweetData tweetIdToTweetData(long id){
        TweetData tweetData = new TweetData(); // ツイートデータ
        try {
            Twitter twitter = new TwitterFactory().getSingleton();
            Status twStatus = twitter.showStatus(id);
            tweetData.setId(twStatus.getId()); // ツイートidをセット
            tweetData.setText(twStatus.getText()); // ツイート本文をセット
            for (MediaEntity mediaEntity : twStatus.getMediaEntities()) {

                // 動画URL取得
                for(Variant variant : mediaEntity.getVideoVariants()) {
                    // 動画URLか判定
                    if(variant.getUrl().indexOf(".mp4") != -1) {
                        tweetData.setVideoUrl(variant.getUrl()); // 動画URLセット
                    }
                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return tweetData;
    }
}
