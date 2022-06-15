package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetData {

    private Long id; // ツイートid

    private String text; // ツイート本文

    private String videoUrl; // 動画url
}
