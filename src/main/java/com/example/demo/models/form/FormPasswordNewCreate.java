package com.example.demo.models.form;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.demo.constants.MessageConst;
import com.example.demo.models.OnetimePassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormPasswordNewCreate implements Serializable {

    private static final long serialVersionUID = 1L;
    // id
    private Integer id;

    // メールアドレス
    @NotBlank(message=MessageConst.VALID_EMAIL)
    @Email(message=MessageConst.VALID_EMAIL)
    private String email;

    // ワンタイムパスワードテーブルid
    private OnetimePassword otp;
}
