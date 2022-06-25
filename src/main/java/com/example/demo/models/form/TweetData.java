package com.example.demo.models.form;

import com.example.demo.models.Tweet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetData {

    private Tweet twe; // ツイート

    private String text; // ツイート本文

    private String videoUrl; // 動画url

    private boolean favoriteFlag; // お気に入りフラグ true : お気に入り , false : お気に入り解除
}
