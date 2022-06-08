package com.example.demo.models.form;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.example.demo.constants.JpaConst;
import com.example.demo.constants.MessageConst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// アカウントデータのフォーム用クラス
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormAccount implements Serializable{

    private static final long serialVersionUID = 1L;

    // グループ作成
    public static interface CreateData {}
    public static interface NewPasswordData {}

    // id
    private Integer id;

    // アカウント名
    @NotEmpty(groups= { CreateData.class } ,message=MessageConst.VALID_NAME)
    private String name;

    // メールアドレス
    private String email;

    // パスワード
    @NotBlank(groups= { CreateData.class,NewPasswordData.class },message=MessageConst.VALID_PASSWORD_NOT_BLANK)
    @Length(min=JpaConst.PASSWORD_MIN,groups= { CreateData.class,NewPasswordData.class },message=MessageConst.VALID_PASSWORD_MIN)
    private String password;

    // パスワード確認
    private String passwordConfirm;

    // 管理者権限
    private Integer adminFlag;

    // 作成日
    private LocalDateTime createdAt;

    // 更新日
    private LocalDateTime updatedAt;

    // 削除日
    private LocalDateTime deletedAt;

    // パスワードの相関チェック
    @AssertTrue(groups= { CreateData.class,NewPasswordData.class },message=MessageConst.VALID_PASSWORD_CONFIRM)
    public boolean isPasswordValid() {
        if(password == null || password.isBlank()) {
            return true;
        }

        return password.equals(passwordConfirm);
    }



}
