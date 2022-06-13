package com.example.demo.securitys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.services.AccountService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService acService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                            "/images/**",
                            "/css/**",
                            "/javascript/**"
                            );
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(acService).passwordEncoder(passwordEncoder());
    }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    //「login.html」はログイン不要でアクセス可能に設定
                    .antMatchers("/login").permitAll()
                    // アカウント新規作成ページをログイン不要でアクセス可能に設定
                    .antMatchers("/accountNewCreate/**").permitAll()
                    // パスワード再発行ページをログイン不要でアクセス可能に設定
                    .antMatchers("/passwordNewCreate/**").permitAll()
                    //上記以外は直リンク禁止
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/tweetSearch")
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutUrl("/logout") //ログアウトのURL
                    .invalidateHttpSession(true)
                    //ログアウト時の遷移先 POSTでアクセス
                    .logoutSuccessUrl("/afterLogout.html");
        }

        // フォームの値と比較するDBから取得したパスワードは暗号化されているのでフォームの値も暗号化するために利用
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {

            return new BCryptPasswordEncoder();
        }

}
