package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.SearchResult;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult,Integer> {

}
