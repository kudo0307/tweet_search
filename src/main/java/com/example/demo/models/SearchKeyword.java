package com.example.demo.models;

import java.time.LocalDateTime;

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

// 検索キーワードのDTOモデル

@Table(name="search_keyword")

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SearchKeyword{


    // id
    @Id
    @Column(name = "srk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // アカウント
    @ManyToOne
    @JoinColumn(name="srk_acc_id",nullable=false)
    private Account ac;

    // キーワード
    @Column(name="srk_keyword", length=255, nullable=false)
    private String keyword;

    // 検索日
    @Column(name="srk_search_at", nullable = false)
    private LocalDateTime searchAt;

}
