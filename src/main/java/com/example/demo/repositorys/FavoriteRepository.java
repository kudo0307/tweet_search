package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {
    @Query(value="SELECT fvt FROM Favorite AS fvt WHERE fvt.twe.tweetId = :tweetId AND fvt.ac.id = :accountId")
    Favorite find(long tweetId,Integer accountId);
}
