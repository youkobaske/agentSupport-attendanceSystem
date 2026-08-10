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
                .requestMatchers("/AS001", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/AS009", "/AS010", "/AS011").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

            // ログイン設定
            .formLogin(form -> form
                .loginPage("/AS001")
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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}