package com.example.demo.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("favoriteTweet")
public class TweetApi {

    @RequestMapping("/create")
    public Map create() {
        Map<String, Object> res = new HashMap<>();
        return res;
    }
}
