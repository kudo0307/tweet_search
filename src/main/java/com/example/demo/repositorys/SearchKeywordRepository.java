package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.SearchKeyword;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword,Integer> {
    @Query(value="SELECT srk FROM SearchKeyword AS srk WHERE srk.id = :id AND srk.ac.id = :acId")
    SearchKeyword findByIdIsAndAcIs(Integer id,Integer acId);
}
