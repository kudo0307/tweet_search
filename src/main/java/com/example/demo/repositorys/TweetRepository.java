package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet,Integer> {
    @Query(value="SELECT twe FROM Tweet AS twe WHERE twe.tweetId = :tweetId")
    Tweet findByTweetIdIs(long tweetId);
}
