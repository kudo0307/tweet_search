package com.example.demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncryptUtil {
    // パスワードをハッシュ化する
    // @param password ハッシュ化するパスワード
    // @return ハッシュ化したパスワード
    public static String passwordEncode(String password) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;
        return passwordEncoder.encode(password);
    }
}
