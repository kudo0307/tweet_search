package com.example.demo.models.validators;

import java.time.LocalDateTime;

public abstract class ValidatorBase {

    // LocalDateTimeにperseできるか判定する
    // @param ldtStr 文字列の日付
    // @return true : 可能 , false : 不可能
    public static Boolean localDateTimePerseCheck(String ldtStr) {

        if(ldtStr == null || ldtStr.equals("")) {
            // NULLまたは空文字なら
            return false;
        }

        try {
            LocalDateTime castAt = LocalDateTime.parse(ldtStr);
        }catch(Exception e) {
            return false;
        }

        return true;
    }

}
