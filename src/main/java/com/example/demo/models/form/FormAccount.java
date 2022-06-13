package com.example.demo.models.form;

import java.io.Serializable;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    public static interface EditData{}
    public static interface UpdateData{}

    // id
    @NotNull(groups={EditData.class})
    private Integer id;

    @Email(groups= {UpdateData.class}, message=MessageConst.VALID_EMAIL)
    // メールアドレス
    private String email;

    // パスワード
    @NotBlank(
            groups= {
                    CreateData.class,
                    NewPasswordData.class,
                    UpdateData.class
                    },
            message=MessageConst.VALID_PASSWORD_NOT_BLANK)
    @Length(min=JpaConst.PASSWORD_MIN,groups= { CreateData.class,NewPasswordData.class,UpdateData.class },message=MessageConst.VALID_PASSWORD_MIN)
    private String password;

    // パスワード確認
    private String passwordConfirm;

    // パスワードの相関チェック
    @AssertTrue(groups= { CreateData.class,NewPasswordData.class,UpdateData.class },message=MessageConst.VALID_PASSWORD_CONFIRM)
    public boolean isPasswordValid() {
        if(password == null || password.isBlank()) {
            return true;
        }

        return password.equals(passwordConfirm);
    }



}
