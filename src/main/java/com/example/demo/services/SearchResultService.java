package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.SearchKeyword;
import com.example.demo.models.SearchResult;
import com.example.demo.models.Tweet;
import com.example.demo.models.TweetData;
import com.example.demo.repositorys.SearchResultRepository;

@Service
public class SearchResultService {

    @Autowired
    private SearchResultRepository srtRepository;

    /* 検索結果テーブルへデータ登録
     * @param tweetDataList ツイートデータリスト
     * @param srk 検索キーワードデータ
     * */
    public void create(List<TweetData> tweetDataList ,SearchKeyword srk) {



        if(tweetDataList == null) {
            return;
        }
        for(TweetData tweetData:tweetDataList) {
            SearchResult saveSrt = new SearchResult();
            Tweet twe = new Tweet();
            saveSrt.setTwe(tweetData.getTwe()); // ツイートデータセット
            saveSrt.setSrk(srk); // 検索キーワードデータセット
            srtSave(saveSrt); // 登録
        }
    }

    // 検索結果テーブルへデータ登録、更新する
    // @param srk 検索キーワードデータ
    // @return 登録、更新した検索結果データ
    public SearchResult srtSave(SearchResult srt) {
        return srtRepository.save(srt);
    }
}
