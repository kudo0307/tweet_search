package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 検索結果データのDTOモデル
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="search_result")
public class SearchResult {

    // id
    @Id
    @Column(name = "srt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ツイートid
    @Column(name="srt_tweet_id")
    private long  tweetId;

    // 検索キーワードid
    @ManyToOne
    @JoinColumn(name="srt_srk_id")
    private SearchKeyword srk;
}

