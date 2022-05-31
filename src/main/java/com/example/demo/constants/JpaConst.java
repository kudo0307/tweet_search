package com.example.demo.constants;

// DB関連の項目値を定義するインターフェース
public interface JpaConst {

    // persistence-unit名
    String PERSISTENCE_UNIT_NAME = "tweet_search";

    // twitterAPI接続key
    String OAUTH_CONSUMER_KEY = "lYHg71gP1OMQdoMVdUvvAPQKQ";
    String OAUTH_CONSUMER_SECRET = "46KZpVpO9tJjdNExFdkJQBvAnlszqLPCDAaaw7FHtQyaPvbhXo";
    String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAFkzcwEAAAAAN25jW82k8G5PB483GL9fuoBZuI0%3D2ZqaqMxY5UwoRVpoxk1pwcUyMquQldteyYEi9AwQ27TEOlE4hH";


    int ROLE_ADMIN = 1; // 管理者権限(管理者)
    int ROLE_GENERAL = 0; // 管理者権限(一般)
}
