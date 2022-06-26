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


// お気に入りデータのDTOモデル
@Table(name="favorite")

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    // id
    @Id
    @Column(name = "fvt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ツイートid
    @ManyToOne
    @JoinColumn(name="fvt_twe_id")
    private Tweet twe;

    // アカウントデータ
    @ManyToOne
    @JoinColumn(name="fvt_acc_id")
    private Account ac;

    // お気に入りフラグ true : お気に入り , false : お気に入り解除
    @Column(name="fvt_flag")
    private boolean flag;

    // 作成日
    @Column(name="fvt_created_at", nullable=false)
    private LocalDateTime createdAt;

    // 更新日
    @Column(name="fvt_updated_at", nullable=false)
    private LocalDateTime updatedAt;
}
