package com.example.demo.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.models.SearchKeyword;
import com.example.demo.repositorys.SearchKeywordRepository;

@Service
public class SearchKeywordService {

    @Autowired
    private SearchKeywordRepository srkRepository;

    /* 検索キーワードテーブルへデータ登録
     * @param id ログインアカウントのID
     * @param keyword 検索キーワード
     * return 検索キーワードデータ
     */

    public SearchKeyword create(@AuthenticationPrincipal Account loginAccount,String keyword) {
        SearchKeyword saveSrk = new SearchKeyword();
        saveSrk.setAc(loginAccount); // アカウント情報セット
        saveSrk.setKeyword(keyword); // 検索キーワードセット
        saveSrk.setSearchAt(LocalDateTime.now()); // 検索日
        return srkSave(saveSrk);// 登録
    }

    /* 検索キーワードidとアカウントデータを元に検索キーワードデータを取得
     * @param id 検索キーワードid
     * @param ac アカウントデータ
     * @return 検索キーワードデータ
     */
    public SearchKeyword getKeyword(Integer id, Integer acId) {
        return srkRepository.findByIdIsAndAcIs(id,acId);
    }

    // 検索キーワードテーブルへデータ登録、更新する
    // @param srk 検索キーワードデータ
    // @return 登録、更新した検索キーワードデータ
    public SearchKeyword srkSave(SearchKeyword srk) {

        return srkRepository.save(srk);
    }
}
