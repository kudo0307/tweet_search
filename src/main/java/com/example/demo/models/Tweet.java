package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// ツイートテーブルのDTOモデル
@Table(name="tweet")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {


    // id
    @Id
    @Column(name = "twe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ツイートID
    @Column(name="twe_tweet_id",nullable=false,unique=true)
    private long tweetId;

    // ツイートURL
    @Column(name="twe_url", length=255,nullable=false)
    private String url;

    // 動画URL
    @Column(name="twe_video_url", length=255)
    private String videoUrl;
}
