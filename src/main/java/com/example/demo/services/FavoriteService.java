package com.example.demo.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.models.Favorite;
import com.example.demo.models.Tweet;
import com.example.demo.repositorys.FavoriteRepository;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository fvtRepository;

    /* お気に入りテーブルへデータ登録
     * @param tweetId ツイートID
     * @param ac アカウントデータ
     * @return お気に入りデータ
     * */
    public Favorite create(Tweet twe,Account ac) {
        Favorite fvt = new Favorite();
        fvt.setTwe(twe); // ツイートデータセット
        fvt.setAc(ac); // アカウントデータセット
        fvt.setFlag(true); // 登録時はお気に入り
        fvt.setCreatedAt(LocalDateTime.now()); // 作成日
        fvt.setUpdatedAt(LocalDateTime.now()); // 更新日
        return fvtSave(fvt);
    }

    /* お気に入りフラグの更新
     * @param ac アカウントデータ
     * @return お気に入りデータ
     * */
    public Favorite update(Favorite fvt) {
        if(fvt.isFlag()) {
            fvt.setFlag(false); // お気に入り解除
        }else {
            fvt.setFlag(true); // お気に入り
        }
        fvt.setUpdatedAt(LocalDateTime.now()); // 更新日
        return fvtSave(fvt);
    }

    /* ツイートIDとアカウントIDを元にお気に入りデータ取得
     * @param tweetId ツイートID
     * @param accountId アカウントID
     * @return お気に入りデータ
     * */
    public Favorite search(long id,Integer accountId) {
        return fvtRepository.find(id, accountId);
    }

    /* お気に入りテーブルへデータ登録、更新する
     *  @param fvt お気に入りデータ
     *  @return 登録、更新したお気に入りデータ
     */
    public Favorite fvtSave(Favorite fvt) {
        return fvtRepository.save(fvt);
    }
}
