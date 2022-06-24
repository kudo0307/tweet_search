package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Tweet;
import com.example.demo.repositorys.TweetRepository;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweRepository;

    /* ツイートIDを元にツイートデータ取得
     * @param tweetId ツイートID
     * @return ツイートデータ
     * */

    public Tweet search(Long tweetId) {

        return tweRepository.findByTweetIdIs(tweetId);
    }

    /* ツイートテーブルへデータ登録
     * データがあれば登録せず取得したデータを返す
     * @param tweetData ツイートデータ
     */
    public Tweet create(Tweet twe) {
        // ツイートIDでツイートデータを検索
        Tweet searchTwe = search(twe.getTweetId());
        if(searchTwe !=null) {
            return searchTwe;
        }
        return tweRepository.save(twe);
    }
}
