package com.example.demo.constants;

// 定数クラス
public interface CommonConst {

    // twitterAPI接続key
    String OAUTH_CONSUMER_KEY = "lYHg71gP1OMQdoMVdUvvAPQKQ";
    String OAUTH_CONSUMER_SECRET = "46KZpVpO9tJjdNExFdkJQBvAnlszqLPCDAaaw7FHtQyaPvbhXo";
    String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAFkzcwEAAAAAN25jW82k8G5PB483GL9fuoBZuI0%3D2ZqaqMxY5UwoRVpoxk1pwcUyMquQldteyYEi9AwQ27TEOlE4hH";

    // tweet検索時の取得件数
    String TWEET_MAX_RESULT = "10";

    // 二重送信防止トークン
    // 生成文字数
    int PROTECTION_TOKEN_INT = 20;
    // 文字列
    String PROTECTION_TOKEN_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
}
