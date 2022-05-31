CREATE TABLE IF NOT EXISTS account(
    acc_id INT PRIMARY KEY NOT NULL  AUTO_INCREMENT,
    acc_name VARCHAR(50) NOT NULL,
    acc_email VARCHAR(255) NOT NULL,
    acc_pass VARCHAR(255) NOT NULL,
    acc_admin_flag INT NOT NULL,
    acc_created_at DATETIME NOT NULL,
    acc_updated_at DATETIME NOT NULL,
    acc_deleted_at DATETIME
);

CREATE TABLE IF NOT EXISTS account_new_create(
    anc_id  INT PRIMARY KEY NOT NULL  AUTO_INCREMENT,
    anc_email VARCHAR(255) NOT NULL,
    anc_otp_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS onetime_password(
    otp_id  INT PRIMARY KEY NOT NULL  AUTO_INCREMENT,
    otp_token VARCHAR(10) NOT NULL,
    otp_token_at DATETIME NOT NULL,
    otp_deleted_at DATETIME
);
CREATE TABLE IF NOT EXISTS password_new_create(
    pnc_id INT PRIMARY KEY NOT NULL  AUTO_INCREMENT,
    pnc_acc_id INT NOT NULL,
    pnc_otp_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS search_keyword(
    srk_id INT PRIMARY KEY NOT NULL  AUTO_INCREMENT,
    srk_acc_id INT NOT NULL,
    srk_keyword VARCHAR(255) NOT NULL,
    srk_search_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS search_result(
    srt_id INT PRIMARY KEY NOT NULL  AUTO_INCREMENT,
    srt_tweet_id VARCHAR(100) NOT NULL,
    srt_srk_id INT NOT NULL
);
