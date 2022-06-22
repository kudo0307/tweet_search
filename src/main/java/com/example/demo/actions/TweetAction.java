package com.example.demo.actions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.actions.views.TweetConverter;
import com.example.demo.constants.CommonConst;
import com.example.demo.models.Account;
import com.example.demo.models.SearchKeyword;
import com.example.demo.models.TweetData;
import com.example.demo.services.SearchKeywordService;
import com.example.demo.services.SearchResultService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TweetAction {

    @Autowired
    private SearchKeywordService srkService;
    @Autowired
    private SearchResultService srtService;
    @RequestMapping("/tweetSearch")
    public String search(@RequestParam(defaultValue = "") String query,@RequestParam(required = false) String nextToken,@RequestParam(required = false) Integer searchId,@AuthenticationPrincipal Account loginAccount,Model model) throws IOException, URISyntaxException {

        // ツイートデータリスト
        List<TweetData> tweetDataList = new ArrayList<>();
        // 検索キーワードが空ではないか判定
        if (!query.isBlank()&&query != null) {

            /* 検索キーワードにオプション追加
             * -is:retweet リツイート除外
             * has:videos 動画付きツイート取得
            */
            String searchTarget = query + "& -is:retweet has:videos";
            // ツイートデータ取得
            String response = search(searchTarget, nextToken ,CommonConst.BEARER_TOKEN);

            // mapper
            ObjectMapper mapper = new ObjectMapper();
            // JsonNodeに変換
            JsonNode json = mapper.readTree(response);
            // ツイートデータを取得できたか判定
            if(json!= null &&json.get("meta").get("result_count").asInt()!=0) {
                // 取得できなかったデータの続きを取得するためのトークン
                nextToken = json.get("meta").get("next_token").asText();
                // ツイートデータリスト生成
                tweetDataList = TweetConverter.jsonNodeToTweetList(json);
            }
            SearchKeyword srk = new SearchKeyword();
            if(nextToken != null&&searchId !=null) {
                // 検索キーワードidとログインアカウントidを元に検索キーワードデータを取得
                SearchKeyword getSrk = srkService.getKeyword(searchId,loginAccount.getId());
                if(getSrk !=null) {
                    // データがあれば上書き
                    srk = getSrk;
                }
            }else {
                // 検索キーワードを登録
                srk = srkService.create(loginAccount,query);
            }
            // 検索結果を登録
            srtService.create(tweetDataList, srk);
            model.addAttribute("searchId",srk.getId());


        }
        model.addAttribute("twitters",tweetDataList); // ツイートリスト
        model.addAttribute("query",query); // 検索キーワード
        model.addAttribute("nextToken",nextToken);
        // 画面遷移
        return "views/index.html";
    }


    /* ツイート検索
     * @param query 検索キーワード
     * @param next_token 次のデータ取得するためのトークン
     * @param bearerToken 認証用トークン
     * @return 検索結果
     * */
    private static String search(String query,String next_token, String bearerToken) throws IOException, URISyntaxException {
        String searchResponse = null;

        HttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build())
            .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", query));
        queryParameters.add(new BasicNameValuePair("max_results", CommonConst.TWEET_MAX_RESULT));
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at,id,text"));
        if(next_token!= null && !next_token.isBlank()) {
            queryParameters.add(new BasicNameValuePair("next_token", next_token));
        }
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
          searchResponse = EntityUtils.toString(entity, "UTF-8");
        }
        return searchResponse;
      }
}


