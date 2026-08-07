package com.example.attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 認証設定
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/AS001", "/AS002", "/css/**", "/js/**", "/images/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            )

            // ログイン設定
            .formLogin(login -> login
                .loginPage("/AS001")
                .loginProcessingUrl("/AS001")
                .defaultSuccessUrl("/AS002", true)
                .failureUrl("/AS001?error")
                .permitAll()
            )

            // ログアウト
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/AS001")
            )

            // CSRF（開発中のみ無効化）
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}